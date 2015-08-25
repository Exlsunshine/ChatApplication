# -*- coding: utf-8 -*-
__author__ = 'LJ'


def getOnLineNumFromDataBase():
    return 300

def getStartAppNumFromDataBase():
    return 3000

def getDownloadNumFromDataBase():
    return 300

def getMaxOnlineNumFromDataBase():
    return 573

def getPerHourOnLineNumFromDataBase():
    import random
    import time
    list = []


    list.append(200)
    list.append(176)
    list.append(130)
    list.append(94)
    list.append(76)
    list.append(85)
    list.append(108)
    list.append(132)
    list.append(158)
    list.append(187)
    list.append(213)
    list.append(230)
    list.append(223)
    list.append(237)
    list.append(256)
    list.append(243)
    list.append(232)
    list.append(254)
    list.append(230)
    list.append(270)
    list.append(263)
    list.append(267)
    list.append(220)
    list.append(198)


    #for i in range(0,int(time.strftime("%H"))):
    #    list.append(random.randint(50,100))
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