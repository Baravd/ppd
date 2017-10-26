using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    class Pereche
    {
        public int i;
        public int j;

        public Pereche(int i, int j)
        {
            this.i = i;
            this.j = j;
        }

        public int J
        {
            get => j;
            set => j = value;
        }

        public int I
        {
            get => i;
            set => i = value;
        }

        public int getI()
        {
            return i;
        }

        public void setI(int i)
        {
            this.i = i;
        }

        public int getJ()
        {
            return j;
        }

        public void setJ(int j)
        {
            this.j = j;
        }

        
        public override String ToString()
        {
            return "Pereche{" +
                   "i=" + i +
                   ", j=" + j +
                   '}';
        }
    }
}