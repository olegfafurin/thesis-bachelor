import math as m
import sys


# computes an average upper bound on distortion for the leaves in a binary tree after FRT
def estimation(n):
    csum = list()
    h = int(m.log2(n))
    print(h)
    print(f"range {h}")
    for i in range(1, int(m.sqrt(2 * h))):
        print(i ** 2)
        csum.append((i ** 2) * (2 ** i))
    for i in range(int(m.sqrt(2 * h)), h + 1):
        print(2 * h / i)
        csum.append((2 * h / i) * 2 ** i)
    print(" ".join(list(map(str, csum))))
    print(sum(csum))
    return sum(csum)


def main():
    filename = sys.argv[1]
    file = open(filename, "w+")
    for i in list(map(lambda x: 2 ** x, range(4, int(sys.argv[2])))):
        file.write(str(estimation(i) / (i - 1)) + "\n")
    file.close()


if __name__ == "__main__":
    main()
