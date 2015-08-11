__author__ = 'LJ'


from database.SQLServerEnd import SQLServerEnd

USER_BASIC_TABLE_NAME = "user_basic_info"

def getMaleNumFromDataBase():
    conditionList = ["sex"]
    conditionValList = ["male"]
    operatorList =  ["="]
    sqlEnd = SQLServerEnd(USER_BASIC_TABLE_NAME)
    maleNum = sqlEnd.selectCount(conditionList, conditionValList, operatorList)
    return maleNum

def getFemaleNumFromDataBase():
    conditionList = ["sex"]
    conditionValList = ["female"]
    operatorList =  ["="]
    sqlEnd = SQLServerEnd(USER_BASIC_TABLE_NAME)
    femaleNum = sqlEnd.selectCount(conditionList, conditionValList, operatorList)
    return femaleNum

def getSexDictFromDataBase():
    maleNum = getMaleNumFromDataBase()
    femaleNum = getFemaleNumFromDataBase()
    dict = {}
    dict['male'] = maleNum
    dict['female'] = femaleNum
    return dict


def main():
    print getMaleNumFromDataBase()


if __name__ == '__main__':
    main()
