__author__ = 'LJ'

import pymssql

class MSSQL:
    def __init__(self, host, user, pwd, db):
        self.host = host
        self.user = user
        self.pwd = pwd
        self.db = db


    def connect(self):
        if not self.db:
            raise(NameError, "have not set database info")
        self.conn = pymssql.connect(host = self.host, user = self.user, password = self.pwd, database = self.db, charset = "utf8")
        cur = self.conn.cursor()
        if not cur:
            raise(NameError, "Connect error")
        return cur

    def ExecQuery(self, sql):
        cur = self.connect()
        cur.execute(sql)
        resList = cur.fetchall()
        self.conn.close()
        return resList


def main():
    ms = MSSQL(host="localhost", user = "sa", pwd = "007", db = "JMMSRDB")
    resList = ms.ExecQuery("select count(*) from user_basic_info where sex = 'male'")
    for (nums) in resList:
        print nums


if __name__ == '__main__':
    main()