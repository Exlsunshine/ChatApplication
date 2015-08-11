# -*- coding: utf-8 -*-
__author__ = 'LJ'

from database.SQLServerEnd import SQLServerEnd

USER_BASIC_TABLE_NAME = "user_basic_info"
PROVINCE_DICT = {u'北京市': 'bj', u'天津市':'tj', u'上海市':'sh', u'浙江省':'zj', u'甘肃省':'gs', u'宁夏':'nx', u'陕西省':'sa' , u'安徽省':'ah', u'湖北省':'hu',u'广东省':'gd', u'福建省':'fj', u'河北省':'hb', u'山东省':'sd', u'江苏省':'js'
                 ,u'海南省':'ha', u'青海省':'qh', u'吉林省':'jl', u'西藏':'xz', u'新疆':'xj', u'河南省':'he', u'内蒙古':'nm', u'黑龙江省':'hl', u'云南省':'yn', u'广西省':'gx', u'辽宁省':'ln', u'四川省':'sc', u'重庆市':'cq', u'贵州省':'gz'
                 ,u'湖南省':'hn', u'山西省':'sx', u'江西省':'jx'}

def getMapDictFromDataBase():
    dict = {}
    for k in PROVINCE_DICT.keys():
        dict[PROVINCE_DICT[k]] =  0

    sqlEnd = SQLServerEnd(USER_BASIC_TABLE_NAME)
    resultList = sqlEnd.excecuteRawQuery("select hometown from user_basic_info")

    for hometown in resultList:
        province = hometown[0].split()[0]
        try:
            dict[PROVINCE_DICT[province]] = dict[PROVINCE_DICT[province]] + 1
        except:
            print province + u"不存在列表中"
    return dict


def main():
    getMapDictFromDataBase()


if __name__ == '__main__':
    main()