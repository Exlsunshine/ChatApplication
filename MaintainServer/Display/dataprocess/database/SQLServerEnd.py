__author__ = 'LJ'

from MySql import MSSQL


class SQLServerEnd:
    def __init__(self, tableName):
        self.USER = 'sa'
        self.PWD = '007'
        self.IPADDRESS = '127.0.0.1'
        self.DATABASE = 'JMMSRDB'
        self.tableName = tableName
        self.sqlExecutor = MSSQL(host=self.IPADDRESS, user = self.USER, pwd = self.PWD, db =  self.DATABASE)

    def buildContidionString(self, conditionList, conditionValList, operatorList):
        str = ""
        for i in range(0, len(conditionList)):
            condtion = conditionList[i]
            conditionVal = conditionValList[i]
            operator = operatorList[i]
            str = str + condtion + " " + operator + " '" + conditionVal + "'"
            if i != len(conditionList) - 1:
                str = str + " and "
        return str



    def selectCount(self, conditionList, conditionValList, operatorList):
        sqlString = "select count(*) from " + self.tableName + " where " + self.buildContidionString(conditionList, conditionValList, operatorList)
        count = self.sqlExecutor.ExecQuery(sqlString)
        return count[0][0]


    def excecuteRawQuery(self, sqlString):
        return self.sqlExecutor.ExecQuery(sqlString)

def main():
    s = SQLServerEnd("user_basic_info")
    conditionList = ["sex"]
    conditionValList = ["male"]
    operatorList =  ["="]
    print s.selectCount(conditionList, conditionValList, operatorList)


if __name__ == '__main__':
    main()