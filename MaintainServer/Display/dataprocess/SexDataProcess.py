__author__ = 'LJ'


from commons.MySql import *

DATABASE = 'JMMSRDB'
USER = 'sa'
PWD = '007'
IPADDRESS = '127.0.0.1'

def getMaleNumFromDataBase():
    ms = MSSQL(host=IPADDRESS, user = USER, pwd = PWD, db = DATABASE)
    maleNum = ms.ExecQuery("select count(*) as num from user_basic_info where sex = 'male'")
    return maleNum[0][0]

def getFemaleNumFromDataBase():
    ms = MSSQL(host=IPADDRESS, user = USER, pwd = PWD, db = DATABASE)
    femaleNum = ms.ExecQuery("select count(*) from user_basic_info where sex = 'female'")
    return femaleNum[0][0]

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
