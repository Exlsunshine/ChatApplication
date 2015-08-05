# -*- coding: utf-8 -*-
__author__ = 'LJ'

PROVINCE_DICT = {'北京': 'bj', '天津':'tj', '上海':'sh', '浙江':'zj', ' 甘肃':'gs', '宁夏':'nx', '陕西':'sa' , '安徽':'ah', '湖北':'hu', '广东':'gd', '福建':'fj', '河北':'hb', '山东':'sd', '江苏':'js'
                 ,'海南':'ha', '青海':'qh', '吉林':'jl', '西藏':'xz', '新疆':'xj', '河南':'he','内蒙':'nm', '黑龙江':'hl', '云南':'yn', '广西':'gx', '辽宁':'ln', '四川':'sc', '重庆':'cq', '贵州':'gz'
                 ,'湖南':'hn', '山西':'sx', '江西':'jx'}

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

def getMapDistributionDictFromDataBase():
    import random
    dict = {}
    for k in PROVINCE_DICT.keys():
        dict[PROVINCE_DICT[k]] =  random_num = random.randint(100,500)
    return dict

def main():
    dict = getSexSatisticsDictFromDataBase()
    print dict['male']

if __name__ == '__main__':
    main()