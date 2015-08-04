__author__ = 'LJ'


def getActiveDic(num,count):
    for var in range(0, count):
        if (num != var):
            dict[var] = ''
        else:
            dict[var] = 'active'
    return dict