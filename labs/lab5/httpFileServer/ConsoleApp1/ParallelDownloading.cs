using System.Collections.Concurrent;
using System.Collections.Generic;
using System.Net;
using System.Threading.Tasks;
using System.Timers;
using Timer = System.Threading.Timer;

namespace ConsoleApp1
{
    public class ParallelDownloading
    {
        private ConcurrentQueue<DownloadFile> _queueToDownlaod;
        private IList<Task> _downloadingTasks;
        private Timer _downloadTimer;
        private int _parallelDownloads;

        public ParallelDownloading(int parallelDownloads)
        {
            _queueToDownlaod = new ConcurrentQueue<DownloadFile>();
            _downloadingTasks = new List<Task>();
            // _downloadTimer = new Timer();

            _parallelDownloads = parallelDownloads;

            // _downloadTimer.Elapsed += new ElapsedEventHandler(DownloadTimer_Elapsed);
            // _downloadTimer.Interval = 1000;
            //_downloadTimer.Start();

            ServicePointManager.DefaultConnectionLimit = parallelDownloads;
        }
    }
}