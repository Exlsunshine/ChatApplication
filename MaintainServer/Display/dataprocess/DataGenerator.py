# -*- coding: utf-8 -*-
__author__ = 'LJ'


def getOnLineNumFromDataBase():
    return 300

def getStartAppNumFromDataBase():
    return 3000

def getDownloadNumFromDataBase():
    return 300

def getMaxOnlineNumFromDataBase():
    return 300

def getPerHourOnLineNumFromDataBase():
    list = []
    for i in range(0,24):
        list.append(i)
    return list

def getSexSatisticsDictFromDataBase():
    from SexDataProcess import getSexDictFromDataBase
    dict = getSexDictFromDataBase()
    return dict

def getAgeListFromDataBase():
    from AgeDataProcess import getAgeListFromDataBase
    List = getAgeListFromDataBase()
    return List

def getMapDistributionDictFromDataBase():
    from MapDataProcess import getMapDictFromDataBase
    dict = getMapDictFromDataBase()
    return dict

def getRegisterDayDictFromDataBase():
    import random
    dict = {}
    dict['male'] = []
    dict['female'] = []
    for i in range(0,24):
        dict['male'].append(random.randint(0,59))
        dict['female'].append(random.randint(0,59))
    return dict

def getRegisterMonthDictFromDataBase():
    import random
    dict = {}
    dict['male'] = []
    dict['female'] = []
    for i in range(0,30):
        dict['male'].append(random.randint(0,59))
        dict['female'].append(random.randint(0,59))
    return dict

def getRegisterYearDictFromDataBase():
    import random
    dict = {}
    dict['male'] = []
    dict['female'] = []
    for i in range(0,12):
        dict['male'].append(random.randint(0,59))
        dict['female'].append(random.randint(0,59))
    return dict

def getZombieReturnDayDictFromDataBase():
    import random
    dict = {}
    dict['male'] = []
    dict['female'] = []
    for i in range(0,24):
        dict['male'].append(random.randint(0,59))
        dict['female'].append(random.randint(0,59))
    return dict

def getZombieReturnMonthDictFromDataBase():
    import random
    dict = {}
    dict['male'] = []
    dict['female'] = []
    for i in range(0,30):
        dict['male'].append(random.randint(0,59))
        dict['female'].append(random.randint(0,59))
    return dict

def getZombieReturnYearDictFromDataBase():
    import random
    dict = {}
    dict['male'] = []
    dict['female'] = []
    for i in range(0,12):
        dict['male'].append(random.randint(0,59))
        dict['female'].append(random.randint(0,59))
    return dict

def buildStackDict(key):
    dict = {}
    dict['online_stack'] = 'nav nav-stacked'
    dict['statistics_stack'] = 'nav nav-stacked'
    dict['distribution_stack'] = 'nav nav-stacked'

    if (key != 'null'):
        dict[key] = 'in nav nav-stacked'
    return dict

def buildActiveDict(key, dict):
    dict[key] = 'active'
    return dict

def buildIconDict(value, dict):
    dict['sec_title_icon'] = value
    return dict

def buildValueDict(key, value, dict):
    dict[key] = value
    return dict

def main():
    dict = getSexSatisticsDictFromDataBase()
    print dict['male']

if __name__ == '__main__':
    main()