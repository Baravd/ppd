using System;
using System.Collections.Generic;
using System.IO;
using System.Net;
using System.Threading.Tasks;

namespace ConsoleApp1
{
    internal class Program
    {
        private static void Main(string[] args)
        {
            List<String> urisList = new List<string>();
            urisList.Add("http://download.thinkbroadband.com/20MB.zip");
            urisList.Add("http://download.thinkbroadband.com/20MB.zip");
            urisList.Add("http://download.thinkbroadband.com/20MB.zip");

            /* var client = new WebClient();

             // Add a user agent header in case the 
             // requested URI contains a query.

             client.Headers.Add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.0.3705;)");

             using (var data = client.OpenRead())
             {
                 Console.WriteLine(data);
                 using (var reader = new StreamReader(data))
                 {
                     var s = reader.ReadToEnd();
                     Console.WriteLine("Here:");
                     var fileLength = client.ResponseHeaders["Content-Length"];

                     Console.WriteLine(fileLength);
                     data.Close();
                     reader.Close();
                 }
             }*/
            if (urisList.Count > 0)
            {
                var tasks = new List<Task>();
                foreach (var url in urisList)
                {
                    async Task Function()
                    {
                        using (var client = new WebClient())
                        {
                            client.Headers.Add("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.2; .NET CLR 1.0.3705;)");

                            using (var data =  client.OpenRead(url))
                            {
                                Console.WriteLine(data);
                                using (var reader = new StreamReader(data))
                                {
                                    var s = await reader.ReadToEndAsync();
                                    Console.WriteLine("Here:");
                                    var fileLength = client.ResponseHeaders["Content-Length"];

                                    Console.WriteLine(fileLength);
                                    data.Close();
                                    reader.Close();
                                }
                            }
                        }
                    }

                    tasks.Add(Task.Factory.StartNew(Function));
                    Task.WaitAll(tasks.ToArray());
                }


                //Task.WaitAll(tasks.ToArray()); // blocking wait

                // could use await here and make this method async:
                // await Task.WhenAll(tasks.ToArray());
                Console.WriteLine("finished");
                // Console.ReadLine();
            }
        }
    }
}