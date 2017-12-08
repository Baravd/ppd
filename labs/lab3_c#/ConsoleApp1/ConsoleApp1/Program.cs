using System;
using System.Collections.Generic;
using System.Diagnostics;
using System.Linq;
using System.Runtime.CompilerServices;
using System.Text;
using System.Threading;
using System.Threading.Tasks;
using System.Windows.Media.Media3D;
using static System.Console;

namespace ConsoleApp1
{
    class Program
    {
        static int N = 100;
        static int K = 100;
        static int M = 100;
        static int rest;
        static int cat, n, w;
        private static Matrice matrice1;
        private static Matrice matrice2;
        private static Matrice rezultat;
        private static List<Pereche> intervale;



        static void Main(string[] args)
        {
            n = N;
            w = 8;
           intervale = new List<Pereche>();

            rest = n % w;
            cat = n / w;
            int stanga = 0;
            int dreapta = stanga + cat - 1;
            rest = rest - 1;
            for (int i = 0; i < w; i++)
            {
                if (rest > 0)
                {
                    intervale.Add(new Pereche(stanga, dreapta));
                    stanga = dreapta + 1;
                    dreapta = stanga + cat;
                    rest = rest - 1;
                }
                else
                {
                    intervale.Add(new Pereche(stanga, dreapta));
                    stanga = dreapta + 1;
                    dreapta = stanga + cat - 1;
                }
            }
            matrice1 = new Matrice(M, K);
            matrice1.populateRandom();
            matrice2 = new Matrice(K, N);
            matrice2.populateRandom();


            rezultat = new Matrice(M, N);

            foreach (var interval in intervale)
            {
                WriteLine(interval.ToString());
            }
            List<Thread> threads = new List<Thread>();
            ParameterizedThreadStart start = new ParameterizedThreadStart(Calcul);
            List<Task> tasks = new List<Task>();
            Stopwatch sw = new Stopwatch();
            sw.Start();

            for (int i = 0; i < w; i++)
            {
               /* Thread thread = new Thread(start);
                threads.Add(thread);
                thread.Start((object) i);*/
                tasks.Add(Task.Factory.StartNew(Calcul, i));

            }
            Task.WaitAll(tasks.ToArray());
            sw.Stop();
            long microseconds1 = sw.ElapsedMilliseconds;
            WriteLine(microseconds1);


            /* foreach (var t in threads)
             {
                 //t.Join();
             }*/
            /*
                        for (int i = 0; i < w; i++)
                        {
                            rezultat.multiply(intervale[i], matrice1, matrice2, rezultat);
                        }

                        WriteLine(rezultat.toString());*/
            ReadKey();
        }

        private static void Calcul( object i)
        {
            int aux = (int) i;
            rezultat.multiply(intervale[aux], matrice1, matrice2, rezultat);
        }
    }
}