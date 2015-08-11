
from django.shortcuts import render
from django.http import HttpResponse
from dataprocess.DataGenerator import *



# Create your views here.


def getRealtimeOnLineNum(request):
    import random
    num = getOnLineNumFromDataBase()
    random_num = random.randint(10,20)
    sign = random.randint(0, 1)
    if (sign == 0):
        num = num + random_num
    else:
        num = num - random_num
    return HttpResponse(num)

def getDownloadNum(request):
    num = getDownloadNumFromDataBase()
    return HttpResponse(num)

def getMaxOnlineNum(request):
    num = getMaxOnlineNumFromDataBase()
    return HttpResponse(num)


def home(request):
    dict = buildStackDict('null')
    dict = buildActiveDict('home_active', dict)
    dict = buildIconDict('icon-home', dict)

    response =  render(request, "home.html", dict)
    return response



def getRealTimeOnlinePage(request):
    dict = buildStackDict('online_stack')
    dict = buildActiveDict('realtimeonline_active', dict)
    dict = buildActiveDict('online_active', dict)
    dict = buildIconDict('icon-group', dict)

    response =  render(request, "Display/subview/realtime_online.html", dict)
    return response

def getPerHourOnlinePage(request):
    dict = buildStackDict('online_stack')
    dict = buildActiveDict('perhouronline_active', dict)
    dict = buildActiveDict('online_active', dict)
    dict = buildIconDict('icon-h-sign', dict)
    dict = buildValueDict('perhouronline_list', getPerHourOnLineNumFromDataBase(), dict)
    response =  render(request, "Display/subview/perhour_online.html", dict)
    return response

def getSexStatisticsPage(request):

    dict = buildStackDict('distribution_stack')
    dict = buildActiveDict('distribution_active', dict)
    dict = buildActiveDict('sex_active', dict)
    dict = buildIconDict('icon-user', dict)

    dict = buildValueDict('sexDict', getSexSatisticsDictFromDataBase(), dict)

    response =  render(request, "Display/subview/sexstatistics.html", dict )
    return response

def getAgeDistributionPage(request):
    dict = buildStackDict('distribution_stack')
    dict = buildActiveDict('distribution_active', dict)
    dict = buildActiveDict('age_active', dict)
    dict = buildIconDict('icon-time', dict)
    dict = buildValueDict('ageDict', getAgeDictFromDataBase(), dict)

    response =  render(request, "Display/subview/agedistribution.html", dict )
    return response

def getMapDistributionPage(request):
    dict = buildStackDict('distribution_stack')
    dict = buildActiveDict('distribution_active', dict)
    dict = buildActiveDict('map_active', dict)
    dict = buildIconDict('icon-globe', dict)
    dict = buildValueDict('mapDict', getMapDistributionDictFromDataBase(), dict)

    response =  render(request, "Display/subview/mapdistribution.html", dict)
    return response

def getRegisterDayPage(request):
    dict = buildStackDict('statistics_stack')
    dict = buildActiveDict('statistics_active', dict)
    dict = buildActiveDict('register_active', dict)
    dict = buildIconDict('icon-smile', dict)
    dict = buildValueDict('registerday_dict', getRegisterDayDictFromDataBase(), dict)
    response =  render(request, "Display/subview/register_day.html", dict)
    return response

def getRegisterMonthPage(request):
    dict = buildStackDict('statistics_stack')
    dict = buildActiveDict('statistics_active', dict)
    dict = buildActiveDict('register_active', dict)
    dict = buildIconDict('icon-smile', dict)
    dict = buildValueDict('registermonth_dict', getRegisterMonthDictFromDataBase(), dict)

    response =  render(request, "Display/subview/register_month.html", dict)
    return response

def getRegisterYearPage(request):
    dict = buildStackDict('statistics_stack')
    dict = buildActiveDict('statistics_active', dict)
    dict = buildActiveDict('register_active', dict)
    dict = buildIconDict('icon-smile', dict)
    dict = buildValueDict('registeryear_dict', getRegisterYearDictFromDataBase(), dict)

    response =  render(request, "Display/subview/register_year.html", dict)
    return response

def getZombieReturnDayPage(request):
    dict = buildStackDict('statistics_stack')
    dict = buildActiveDict('statistics_active', dict)
    dict = buildActiveDict('zombiereturn_active', dict)
    dict = buildIconDict('icon-meh', dict)
    dict = buildValueDict('zombiereturn_day_dict', getZombieReturnDayDictFromDataBase(), dict)

    response =  render(request, "Display/subview/zombie_return_day.html", dict)
    return response

def getZombieReturnMonthPage(request):
    dict = buildStackDict('statistics_stack')
    dict = buildActiveDict('statistics_active', dict)
    dict = buildActiveDict('zombiereturn_active', dict)
    dict = buildIconDict('icon-meh', dict)
    dict = buildValueDict('zombiereturn_month_dict', getZombieReturnMonthDictFromDataBase(), dict)

    response =  render(request, "Display/subview/zombie_return_month.html", dict)
    return response

def getZombieReturnYearPage(request):
    dict = buildStackDict('statistics_stack')
    dict = buildActiveDict('statistics_active', dict)
    dict = buildActiveDict('zombiereturn_active', dict)
    dict = buildIconDict('icon-meh', dict)
    dict = buildValueDict('zombiereturn_year_dict', getZombieReturnYearDictFromDataBase(), dict)

    response =  render(request, "Display/subview/zombie_return_year.html", dict)
    return response

def subview(request):
    pageName = request.GET['pageName']
    if (pageName == 'RealTimeOnline'):
        return getRealTimeOnlinePage(request)
    elif (pageName == 'PerHourOnline'):
        return getPerHourOnlinePage(request)
    elif (pageName == 'SexStatistics'):
        return getSexStatisticsPage(request)
    elif (pageName == 'AgeDistribution'):
        return getAgeDistributionPage(request)
    elif (pageName == 'MapDistribution'):
        return getMapDistributionPage(request)
    elif (pageName == 'RegisterDayPage'):
        return getRegisterDayPage(request)
    elif (pageName == 'RegisterMonthPage'):
        return getRegisterMonthPage(request)
    elif (pageName == 'RegisterYearPage'):
        return getRegisterYearPage(request)
    elif (pageName == 'ZombieReturnDayPage'):
        return getZombieReturnDayPage(request)
    elif (pageName == 'ZombieReturnMonthPage'):
        return getZombieReturnMonthPage(request)
    elif (pageName == 'ZombieReturnYearPage'):
        return getZombieReturnYearPage(request)
    else:
        return home(request)

