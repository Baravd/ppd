from random import Random
from threading import Thread, Lock

l = Lock()
seed = 10000
matriceDeAdj = []
n = 10


def check():
    global seed
    global matriceDeAdj
    l.acquire()
    r = Random()
    r.seed(seed)
    seed += 10000
    l.release()
    for i in range(10000):
        res = True
        nodes = []
        for x in range(n):
            nodes.append(x)
        r.shuffle(nodes)
        for i in range(len(nodes)):
            if not matriceDeAdj[nodes[i]][nodes[(i + 1) % n]] is True:
                res = False
        if res is True:
            print("Cycle")
            return


def main():
    global matriceDeAdj, n
    matriceDeAdj = [[False for _ in range(n)] for _ in range(n)]
    r = Random()
    m = r.randint(int(0.8 * (n * (n - 1))), n * (n - 1))
    while m > 0:
        start = r.randint(0, n - 1)
        end = r.randint(0, n - 1)
        if start != end and not matriceDeAdj[start][end]:
            matriceDeAdj[start][end] = True
            m -= 1
    threads = []
    for i in range(10):
        t = Thread(target=check)
        t.start()
        threads.append(t)
    for t in threads:
        t.join()


main()
