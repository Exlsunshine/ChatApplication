__author__ = 'LJ'


from database.SQLServerEnd import SQLServerEnd
import datetime

USER_BASIC_TABLE_NAME = "user_basic_info"

def getNowDate():
    return datetime.datetime.now().strftime("%Y-%m-%d")

def getYearsAgoDate(years):
    return (datetime.datetime.now() - datetime.timedelta(days = years * 365)).strftime("%Y-%m-%d")

def getAgeCountFromDataBase(begin, end):
    endYear = getYearsAgoDate(begin)
    beginYear = getYearsAgoDate(end)
    conditionList = ["birthday", "birthday"]
    conditionValList = [beginYear, endYear]
    operatorList =  [">=", "<="]
    sqlEnd = SQLServerEnd(USER_BASIC_TABLE_NAME)
    count = sqlEnd.selectCount(conditionList, conditionValList, operatorList)
    return count

def getAgeListFromDataBase():
    list = []
    for i in range(16, 61, 5):
        begin = i
        end = i + 4
        list.append(getAgeCountFromDataBase(begin, end))
    return list




def main():
    print getAgeListFromDataBase()


if __name__ == '__main__':
    main()