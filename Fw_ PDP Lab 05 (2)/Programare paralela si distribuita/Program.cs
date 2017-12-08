using System;
using System.Linq;
using Alea;
using Alea.Parallel;
using System.Numerics;
using System.Text;


namespace CUDA2
{
    public sealed class Polynomial
    {
        // Our random number generator.
        private static readonly Random R = new Random();

        // The degree of the polynomial.
        private readonly int _degree;
        // The array of coefficients. Its length should always be degree + 1.   
        // For example a polynomial with degree 0 has a single coefficient.
        private readonly int[] _coefficients;

        private Polynomial(params int[] coefficients)
        {
            if ((coefficients == null) || (coefficients.Length == 0))
                throw new InvalidOperationException("Invalid coefficients specification!");

            _degree = coefficients.Length - 1;
            _coefficients = coefficients;
        }

        // Next we override the 3 basic methods that all C# objects share.
        public override string ToString()
        {
            // String operations generate a lot of garbage since they are immutable.
            // To avoid this we use a string builder. This is the recommended approach.
            var sb = new StringBuilder();

            for (var i = 0; i <= _degree; ++i)
                sb.AppendFormat("{0}{1}*X^{2}", _coefficients[i] >= 0 ? "+" : "", _coefficients[i], i);

            return sb.ToString();
        }

        public override bool Equals(object obj)
        {
            var polynomial = obj as Polynomial;

            if (_degree != polynomial?._degree)
                return false;

            for (var i = 0; i <= _degree; ++i)
                if (_coefficients[i] != polynomial._coefficients[i])
                    return false;

            return true;
        }

        // Auto implemented by ReSharper.
        public override int GetHashCode()
        {
            unchecked
            {
                return (_degree * 397) ^ _coefficients.GetHashCode();
            }
        }

        // Generates a random polynomial of a given degree.
        public static Polynomial RandomPolynomial(int degree)
        {
            if (degree < 0)
                throw new InvalidOperationException("Invalid degree specification!");

            var coefficients = new int[degree + 1];

            for (var i = 0; i <= degree; ++i)
                coefficients[i] = R.Next(1, 10);

            return new Polynomial(coefficients);
        }

        // Shifts a polynomial to the left by a given ofset.
        // Equivalent to multiplying with "X^offset".
        // For example, X^2 shifted by offset 3 becomes X^5.
        // "^" denotes the power function.
        public static Polynomial Shift(Polynomial p, int offset)
        {
            if (offset < 0)
                throw new InvalidOperationException("Invalid offset specification!");

            var coefficients = new int[p._degree + 1 + offset];
            Array.Copy(p._coefficients, 0, coefficients, offset, p._degree + 1);
            return new Polynomial(coefficients);
        }

        // Overloaded the shift ("<<") operator.
        // Makes working with objects more elegant and concise.
        // Although "behind the scenes" it is still a function of two arguments.
        // Notice that all it does is call the "Shift" static method.
        // For example "p << 3" is equivalent to Shift(p, 3).
        public static Polynomial operator <<(Polynomial p, int offset)
        {
            return Shift(p, offset);
        }

        // Simple addition. No need for parallelization.
        public static Polynomial Add(Polynomial a, Polynomial b)
        {
            var min = Math.Min(a._degree, b._degree);
            var max = Math.Max(a._degree, b._degree);

            var coefficients = new int[max + 1];

            for (var i = 0; i <= min; ++i)
                coefficients[i] = a._coefficients[i] + b._coefficients[i];

            for (var i = min + 1; i <= max; ++i)
                if (i <= a._degree)
                    coefficients[i] = a._coefficients[i];
                else
                    coefficients[i] = b._coefficients[i];

            return new Polynomial(coefficients);
        }

        public static Polynomial operator +(Polynomial a, Polynomial b)
        {
            return Add(a, b);
        }

        // Simple subtraction. No need for parallelization.
        public static Polynomial Subtract(Polynomial a, Polynomial b)
        {
            var min = Math.Min(a._degree, b._degree);
            var max = Math.Max(a._degree, b._degree);

            var coefficients = new int[max + 1];

            for (var i = 0; i <= min; ++i)
                coefficients[i] = a._coefficients[i] - b._coefficients[i];

            for (var i = min + 1; i <= max; ++i)
                if (i <= a._degree)
                    coefficients[i] = a._coefficients[i];
                else
                    coefficients[i] = -b._coefficients[i];

            var degree = coefficients.Length - 1;
            while ((coefficients[degree] == 0) && (degree > 0))
                degree--;

            var clean = new int[degree + 1];
            Array.Copy(coefficients, 0, clean, 0, degree + 1);
            return new Polynomial(clean);
        }

        public static Polynomial operator -(Polynomial a, Polynomial b)
        {
            return Subtract(a, b);
        }

        // Serial multiplication.
        public static Polynomial Multiply(Polynomial a, Polynomial b)
        {
            var coefficients = new int[a._degree + b._degree + 1];

            for (var i = 0; i <= a._degree; ++i)
                for (var j = 0; j <= b._degree; ++j)
                    coefficients[i + j] += a._coefficients[i] * b._coefficients[j];

            return new Polynomial(coefficients);
        }

        [GpuManaged]
        public static Polynomial operator *(Polynomial a, Polynomial b)
        {
            return MultiplyParallel(a, b);
        }

        [GpuManaged]
        // Parallel multiplication.
        public static Polynomial MultiplyParallel(Polynomial a, Polynomial b)
        {
            var gpu = Gpu.Default;

            var coefficients = new int[a._degree + b._degree + 1];
            int[] arg1 = new int[a._coefficients.Length];
            a._coefficients.CopyTo(arg1, 0);
            int[] arg2 = new int[b._coefficients.Length];
            b._coefficients.CopyTo(arg2, 0);
            int degA = a._degree;
            int degB = b._degree;

            gpu.For(0, degA + 1, i =>
            {
                for (var j = 0; j <= degB; ++j) coefficients[i + j] = arg1[i] * arg2[j];
            });

            return new Polynomial(coefficients);
        }

        // The BigInteger class belongs to the System.Numerics namespace.
        // In order to use it, you must add a reference to the System.Numerics assembly.
        // A BigInteger is used to store arbitrarily large signed integers.
        // The function below computes the number of bits for a given integer.
        // http://stackoverflow.com/questions/2187123/optimizing-karatsuba-implementation
        public static int BitLength(BigInteger number)
        {
            var data = number.ToByteArray();
            var result = (data.Length - 1) * 8;
            var msb = data[data.Length - 1]; // "msb" stands for most significant byte.
            while (msb != 0)
            {
                result += 1;
                msb >>= 1;
            }
            return result;
        }

        // Karatsuba multiplication for big numbers.
        public static BigInteger Karatsuba(BigInteger x, BigInteger y)
        {
            const int threshold = 1000;

            var length = Math.Max(BitLength(x), BitLength(y));
            if (length <= threshold)
                return x * y;
            length = (length + 1) / 2;

            var b = x >> length;
            var a = x - (b << length);
            var d = y >> length;
            var c = y - (d << length);

            var ac = Karatsuba(a, c);
            var bd = Karatsuba(b, d);
            var abcd = Karatsuba(a + b, c + d);

            return ac + ((abcd - ac - bd) << length) + (bd << (2 * length));
        }

        [GpuManaged]
        // Parallel Karatsuba multiplication for two polynomials.
        // https://en.wikipedia.org/wiki/Karatsuba_algorithm
        // https://pythonandr.com/2015/10/13/karatsuba-multiplication-algorithm-python-code/
        public static Polynomial Karatsuba(Polynomial x, Polynomial y)
        {
            var gpu = Gpu.Default;

            int[] arg1 = new int[x._coefficients.Length];
            x._coefficients.CopyTo(arg1, 0);
            int[] arg2 = new int[y._coefficients.Length];
            y._coefficients.CopyTo(arg2, 0);
            int degA = x._degree;
            int degB = y._degree;
            int n = x._degree + 1;


            var coefficients = new int[2*n-1];

            var d = new int[n];
            var d2 = new int[n, n];

            gpu.For(0, n, i =>
              {
                  d[i] = arg1[i] * arg2[i];
              });

            gpu.For(0, 2 * n - 2, j =>
                {
                    var bound = j % 2 == 0 ? j / 2 - 1 : j / 2;
                    for(var s=0; s<=bound; s++)
                    {
                        var t = j - s;
                        d2[s, t] = (arg1[s] + arg1[t]) * (arg2[s] + arg2[t]);
                    }
                });

            coefficients[0] = d[0];
            coefficients[2 * n - 2] = d[n - 1];
            gpu.For(1, 2*n-2, i =>
            {
                if (i % 2 == 1)
                {
                    var sum1 = 0;
                    var sum2 = 0;
                    for (var s = 0; s <= i / 2; s++)
                    {
                        var t = i - s;
                        sum1 += d2[s, t];
                        sum2 += d[s] + d[t]; 
                    }
                    coefficients[i] = sum1-sum2;
                }
                else
                {
                    var sum1 = 0;
                    var sum2 = 0;
                    for (var s = 0; s < i / 2; s++)
                    {
                        var t = i - s;
                        sum1 += d2[s, t];
                        sum2 += d[s] + d[t];
                    }
                    coefficients[i] = sum1-sum2 + d[i/2];
                }
            });

                return new Polynomial(coefficients);
        }
    }

    class Program
    {
        [GpuParam]
        public static Polynomial a = Polynomial.RandomPolynomial(1);
        [GpuParam]
        public static Polynomial b = Polynomial.RandomPolynomial(1);
        static void Main(string[] args)
        {
            //var x = BigInteger.One << (50000 - 1);
            //var y = BigInteger.One << (60000 + 1);
            //var rr1 = x * y;
            //Console.WriteLine(x);
            //Console.WriteLine(y);
            //Console.WriteLine(rr1);
            //Console.WriteLine(rk1.Equals(rr1));
            var rr2 = a * b;
            Console.WriteLine(a);
            Console.WriteLine(b);
            //Console.WriteLine(rr2);
            var rk2 = Polynomial.Karatsuba(a, b);

            Console.WriteLine(rk2);
            //Console.ReadLine();
        }
    }
}
