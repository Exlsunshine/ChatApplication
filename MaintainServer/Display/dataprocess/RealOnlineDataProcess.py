__author__ = 'admin'
from database.SQLServerEnd import SQLServerEnd
LOGIN_TABLE_NAME = "login_data"

def getOnlineNum():
    sql = SQLServerEnd(LOGIN_TABLE_NAME)
    conditionList = ['status']
    conditionValList = ['1']
    operatorList = ['=']
    num = sql.selectCount(conditionList,conditionValList,operatorList)
    return num

def getStartNum():
    sql = SQLServerEnd(LOGIN_TABLE_NAME)
    conditionList = ['status']
    conditionValList = ['-10']
    operatorList = ['!=']
    num = sql.selectCount(conditionList,conditionValList,operatorList)
    return num
