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

def getSexSatisticsDictFromDataBase():
    dict = {}
    dict['male'] = 37.3
    dict['female'] = 100 - dict['male']
    return dict

def main():
    dict = getSexSatisticsDictFromDataBase()
    print dict['male']

if __name__ == '__main__':
    main()