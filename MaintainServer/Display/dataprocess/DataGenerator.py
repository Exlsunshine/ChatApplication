# -*- coding: utf-8 -*-
__author__ = 'LJ'

PROVINCE_DICT = {'北京市': 'bj', '天津市':'tj', '上海市':'sh', '浙江省':'zj', ' 甘肃省':'gs', '宁夏':'nx', '陕西省':'sa' , '安徽省':'ah', '湖北省':'hu', '广东省':'gd', '福建省':'fj', '河北省':'hb', '山东省':'sd', '江苏省':'js'
                 ,'海南省':'ha', '青海省':'qh', '吉林省':'jl', '西藏':'xz', '新疆':'xj', '河南省':'he','内蒙古':'nm', '黑龙江省':'hl', '云南省':'yn', '广西省':'gx', '辽宁省':'ln', '四川省':'sc', '重庆市':'cq', '贵州省':'gz'
                 ,'湖南省':'hn', '山西省':'sx', '江西省':'jx'}

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