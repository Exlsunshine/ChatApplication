__author__ = 'LJ'

def getOnLineNumFromDataBase():
    return 300

def getDownloadNumFromDataBase():
    return 300

def getPerHourOnLineNumFromDataBase():
    list = []
    for i in range(0,24):
        list.append(i)
    return list


def main():
    num = getOnLineNumFromDataBase()
    print num

if __name__ == '__main__':
    main()