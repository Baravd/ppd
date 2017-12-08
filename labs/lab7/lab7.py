from multiprocessing.pool import ThreadPool
from random import random

import math
from threading import Thread, Lock

from copy import deepcopy

inputList = []
outputList = []
l = Lock()


def firstPass(begin, end):
    if begin == end:
        return inputList[begin]
    tp = ThreadPool(1)
    mid = (begin + end) // 2
    left = tp.apply_async(func=firstPass, args=(begin, mid)).get()
    right = tp.apply_async(func=firstPass, args=(mid + 1, end)).get()
    # construim sumele intermediare 2 cate  folosind divide et impera (practic o structura de arbore binar pe stiva)
    outputList[end] = left + right
    return left + right


def secondPass(begin, end):
    #facem a doua parcurgere pentru a ca momentam avem doar din doua in doua elementele corecte
    #si elem care este calculat este suma dintre parinte din tree si fratele din stanga
    if begin == end:
        return
    mid = (begin + end) // 2
    l.acquire()
    tmp = outputList[mid]
    outputList[mid] = outputList[end]
    outputList[end] += tmp
    l.release()
    leftT = Thread(target=secondPass, args=(begin, mid))
    rightT = Thread(target=secondPass, args=(mid + 1, end))
    leftT.start()
    rightT.start()
    leftT.join()
    rightT.join()


def main():
    global inputList
    global outputList

    size = 100
    inputList = [x + 1 for x in range(size)]
    outputList = deepcopy(inputList)

    firstPass(0, size - 1)
    tmp = outputList[size - 1]
    outputList[size - 1] = 0
    secondPass(0, size - 1)
    outputList.append(tmp)

    print(inputList)
    print(outputList)

    #algoritm http://www.cs.princeton.edu/courses/archive/fall13/cos326/lec/23-parallel-scan.pdf


main()
