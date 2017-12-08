import numpy as np
import time
import multiprocessing
from ctypes import c_int


def conv(start, end):
    for i in range(start, end):
        for j in range(m):
            res[i + j] += a[i] * b[j]


def conv_parallel(n, a, b, res, start, end):
    for i in range(start, end):
        s = max(i - n.value + 1, 0)
        cnt = 1
        for j in range(s, min(i + 1, n.value)):
            res[i] += a[j] * b[min(i + 1, n.value) - cnt]
            cnt += 1


def karatsuba(n, a, b):
    l = multiprocessing.Value(c_int, int(n.value / 2), lock=False)
    l1 = multiprocessing.Value(c_int, int(n.value / 2), lock=False)

    a0 = a[:l.value]
    a1 = a[l.value:]
    b0 = b[:l.value]
    b1 = b[l.value:]

    l1.value = len(a1)

    r1 = [0 for _ in range(2 * l.value - 1)]
    r2 = [0 for _ in range(2 * l1.value - 1)]
    r3 = [0 for _ in range(2 * l1.value - 1)]
    conv_parallel(l, a0, b0, r1, 0, len(r1))
    conv_parallel(l1, a1, b1, r2, 0, len(r2))
    conv_parallel(l1, [i + j for i, j in zip(a0, a1)], [i + j for i, j in zip(b0, b1)], r3, 0, len(r3))
    r3 = [x - y - z for x, y, z in zip(r3, r2, r1)]

    r1.extend([0 for _ in range(n.value)])
    r2 = [0 for _ in range(n.value)] + r2
    r3 = [0 for _ in range(int(n.value / 2))] + r3 + [0 for _ in range(int(n.value / 2))]

    return [x + y + z for x, y, z in zip(r1, r2, r3)]


def karatsuba_parallel(n, a, b):
    nworkers = 4

    l = multiprocessing.Value(c_int, int(n.value / 2), lock=False)
    l1 = multiprocessing.Value(c_int, int(n.value / 2), lock=False)

    a0 = a[:l.value]
    a1 = a[l.value:]
    b0 = b[:l.value]
    b1 = b[l.value:]

    l1.value = len(a1)

    r1 = [0 for _ in range(2 * l.value - 1)]
    r2 = [0 for _ in range(2 * l1.value - 1)]
    r3 = [0 for _ in range(2 * l1.value - 1)]

    thrs = []
    for i in range(0, (n.value + m.value - 1), int((n.value + m.value - 1) / nworkers)):
        th = multiprocessing.Process(target=conv_parallel, args=(
        l, a0, b0, r1, i, int(min(i + (2 * l.value - 1) / nworkers, (2 * l.value - 1)))))
        thrs.append(th)
        th.start()
        th = multiprocessing.Process(target=conv_parallel, args=(
        l, a1, b1, r2, i, int(min(i + (2 * l.value - 1) / nworkers, (2 * l.value - 1)))))
        thrs.append(th)
        th.start()
        th = multiprocessing.Process(target=conv_parallel, args=(
        l, [i + j for i, j in zip(a0, a1)], [i + j for i, j in zip(b0, b1)], r1, i,
        int(min(i + (2 * l.value - 1) / nworkers, (2 * l.value - 1)))))
        thrs.append(th)
        th.start()
    for th in thrs:
        th.join()

    r3 = [x - y - z for x, y, z in zip(r3, r2, r1)]

    r1.extend([0 for _ in range(n.value)])
    r2 = [0 for _ in range(n.value)] + r2
    r3 = [0 for _ in range(int(n.value / 2))] + r3 + [0 for _ in range(int(n.value / 2))]

    return [x + y + z for x, y, z in zip(r1, r2, r3)]


if __name__ == '__main__':
    n = multiprocessing.Value(c_int, 0, lock=False)
    m = multiprocessing.Value(c_int, 0, lock=False)

    n.value = int(input('Give n: '))
    m.value = int(input('Give m: '))

    nworkers = 4
    a = multiprocessing.Array(c_int, np.random.randint(1, 100, int(n.value)).tolist())
    b = multiprocessing.Array(c_int, np.random.randint(1, 100, int(m.value)).tolist())

    res = multiprocessing.Array(c_int, [int(i) for i in np.zeros(int(n.value) + int(m.value) - 1).tolist()])

    # Single thread n^2
    start = time.time()

    conv_parallel(n, a, b, res, 0, n.value + m.value - 1)

    end = time.time()
    print("n^2 1 thread: %s" % str(end - start))

    res = multiprocessing.Array(c_int, [int(i) for i in np.zeros(int(n.value) + int(m.value) - 1).tolist()])

    # 4 threads n^2
    start = time.time()
    threads = []

    for i in range(0, (n.value + m.value - 1), int((n.value + m.value - 1) / nworkers)):
        th = multiprocessing.Process(target=conv_parallel, args=(
        n, a, b, res, i, int(min(i + (n.value + m.value - 1) / nworkers, (n.value + m.value - 1)))))
        threads.append(th)
        th.start()
    for th in threads:
        th.join()
    end = time.time()
    print("n^2 " + str(nworkers) + " threads: %s" % str(end - start))

    start = time.time()

    res1 = np.convolve(a, b)

    end = time.time()
    print("numpy convolve 1 thread: %s" % str(end - start))

    start = time.time()

    karatsuba(n, a, b)

    end = time.time()
    print("karatsuba 1 thread: %s" % str(end - start))

    start = time.time()

    karatsuba_parallel(n, a, b)

    end = time.time()
    print("karatsuba 4 threads: %s" % str(end - start))
