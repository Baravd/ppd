import operator
import random
import threading
import time

import asyncio

numberTh = 0

threshold = 0
lock = asyncio.Lock()


def multiply(p1, p2):
    final_coeffs = [0] * (len(p2) + len(p1) - 1)
    for ind1, coef1 in enumerate(p1):
        for ind2, coef2 in enumerate(p2):
            final_coeffs[ind1 + ind2] += coef1 * coef2
    return final_coeffs


class ThreadMultiply(threading.Thread):
    def __init__(self, ind1, p2, coef1):
        threading.Thread.__init__(self)
        self.p2 = p2
        self.ind1 = ind1
        self.coef1 = coef1

    def run(self):
        for ind2, coef2 in enumerate(p2):
            final_coeffs[self.ind1 + ind2] += self.coef1 * coef2


@asyncio.coroutine
def ThreadMultiplyKaratsuba(p1, p2):

    # lock.acquire()
    global threshold
    threshold += 1
    # lock.release()

    if threshold >= 100:
        return multiplyKaratsuba(p1, p2)

    if len(p1) == 1 and len(p2) == 1:
        return [p1[0] * p2[0]]

    if len(p1) > len(p2):
        p2 += (len(p1) - len(p2)) * [0]
    if len(p1) < len(p2):
        p1 += (len(p2) - len(p1)) * [0]
    if len(p1) % 2 == 1:
        p1 += [0]
        p2 += [0]

    lenp1 = int(len(p1) / 2)
    lenp2 = int(len(p2) / 2)

    productLow = asyncio.async(ThreadMultiplyKaratsuba(p1[:lenp1], p2[:lenp2]))
    productHigh = asyncio.async(ThreadMultiplyKaratsuba(p1[lenp1:], p2[lenp2:]))
    productLowHigh = asyncio.async(ThreadMultiplyKaratsuba([x + y for x, y in zip(p1[:lenp1], p1[lenp1:])],
                                                           [x + y for x, y in zip(p2[:lenp2], p2[lenp2:])]))

    yield from productLow
    yield from productHigh
    yield from productLowHigh

    productMiddle = list(
        map(operator.sub, list(map(operator.sub, productLowHigh.result(), productHigh.result())), productLow.result()))

    final = (2 * len(p1) - 1) * [0]

    for i in range(len(p1) - 1):
        final[i] += productLow.result()[i]
        final[i + len(p1)] += productHigh.result()[i]
        final[i + lenp1] += productMiddle[i]

    return final


def multiplyKaratsuba(p1, p2):
    if len(p1) == 1 and len(p2) == 1:
        return [p1[0] * p2[0]]

    if len(p1) > len(p2):
        p2 += (len(p1) - len(p2)) * [0]
    if len(p1) < len(p2):
        p1 += (len(p2) - len(p1)) * [0]
    if len(p1) % 2 == 1:
        p1 += [0]
        p2 += [0]

    lenp1 = (int)(len(p1) / 2)
    lenp2 = (int)(len(p2) / 2)

    # z2 = x1*y1
    productLow = multiplyKaratsuba(p1[:lenp1], p2[:lenp2])

    # z0 = x0 * y0
    productHigh = multiplyKaratsuba(p1[lenp1:], p2[lenp2:])

    # (x1 + x0)*(y1 + y0)
    productLowHigh = multiplyKaratsuba([x + y for x, y in zip(p1[:lenp1], p1[lenp1:])],
                                       [x + y for x, y in zip(p2[:lenp2], p2[lenp2:])])

    # z1 = productLowHigh - z2 - z0
    productMiddle = list(map(operator.sub, list(map(operator.sub, productLowHigh, productHigh)), productLow))

    final = (2 * len(p1) - 1) * [0]

    for i in range(len(p1) - 1):
        final[i] += productLow[i]
        final[i + len(p1)] += productHigh[i]
        final[i + lenp1] += productMiddle[i]

    return final


if __name__ == "__main__":
    p1 = [1, 2, 3]
    p2 = [1, 2, 3]

    for i in range(1000):
        p1.append(random.randrange(1, 9))
        p2.append(random.randrange(1, 9))

    start_time = time.time()
    print(multiply(p1, p2))
    print("Time for regular algorithm: " + str(time.time() - start_time))

    start_time = time.time()
    print(multiplyKaratsuba(p1, p2))
    print("Time for the Karatsuba algorithm: " + str(time.time() - start_time))

    start_time = time.time()
    threads = []
    final_coeffs = [0] * (len(p2) + len(p1) - 1)
    for ind1, coef1 in enumerate(p1):
        thread = ThreadMultiply(ind1, p2, coef1)
        thread.start()
        threads.append(thread)

    for t in threads:
        t.join()

    print(final_coeffs)
    print("Time for regular algorithm (paralleled form): " + str(time.time() - start_time))

    start_time = time.time()
    loop = asyncio.get_event_loop()
    result = loop.run_until_complete(ThreadMultiplyKaratsuba(p1, p2))
    print(result)
    print("Time for Karatsuba algorithm (parallelized form): " + str(time.time() - start_time))
