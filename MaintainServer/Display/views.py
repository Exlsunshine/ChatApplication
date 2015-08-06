
from django.shortcuts import render
from django.http import HttpResponse
from dataprocess.DataGenerator import *



# Create your views here.
def line(request):
    return render(request, "line.html")

def column(request):
    sexDict = {'male' : 30.34, 'female' : 40.32}
    return render(request, "column.html", {'sexDict' : sexDict})

def skip(request):
    return render(request, "index.html")

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
    online_stack = 'nav nav-stacked'
    statistics_stack =  'nav nav-stacked'


    home_active = 'active'
    SecTitleIcon = 'icon-dashboard'

    response =  render(request, "home.html", {'home_active' : home_active, 'sec_title_icon' :SecTitleIcon, 'online_stack': online_stack, 'statistics_stack':statistics_stack})
    return response



def getRealTimeOnlinePage(request):
    online_stack = 'in nav nav-stacked'
    statistics_stack =  'nav nav-stacked'


    realtimeonline_active = 'active'
    SecTitleIcon = 'icon-edit'
    online_active = 'active'

    response =  render(request, "Display/subview/realtime_online.html", {'realtimeonline_active' : realtimeonline_active, 'sec_title_icon' :SecTitleIcon
                                                                          ,'online_active':online_active,'online_stack':online_stack, 'statistics_stack':statistics_stack} )
    return response

def getPerHourOnlinePage(request):
    online_stack = 'in nav nav-stacked'
    statistics_stack =  'nav nav-stacked'


    list = getPerHourOnLineNumFromDataBase()
    perhouronline_active = 'active'
    online_active = 'active'
    SecTitleIcon = 'icon-edit'
    response =  render(request, "Display/subview/perhour_online.html", {'perhouronline_active' : perhouronline_active, 'sec_title_icon' :SecTitleIcon, 'perhouronline_list' : list
                                                                        ,'online_active':online_active, 'online_stack':online_stack, 'statistics_stack':statistics_stack} )
    return response

def getSexStatisticsPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'
    SecTitleIcon = 'icon-cogs'
    statistics_active = 'active'
    sex_active = 'active'

    dict = getSexSatisticsDictFromDataBase()
    response =  render(request, "Display/subview/sexstatistics.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'sex_active':sex_active, 'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'sexDict':dict} )
    return response

def getAgeDistributionPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'
    SecTitleIcon = 'icon-list-ul'
    statistics_active = 'active'
    age_active = 'active'

    dict = getAgeDictFromDataBase()
    response =  render(request, "Display/subview/agedistribution.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'age_active':age_active, 'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'ageDict':dict} )
    return response

def getMapDistributionPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-cogs'
    statistics_active = 'active'
    map_active = 'active'

    dict = getMapDistributionDictFromDataBase()
    response =  render(request, "Display/subview/mapdistribution.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'map_active':map_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'mapDict':dict} )
    return response

def getRegisterDayPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-comments'
    statistics_active = 'active'
    register_active = 'active'
    dict = getRegisterDayDictFromDataBase()
    response =  render(request, "Display/subview/register_day.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'register_active':register_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'registerday_dict':dict} )
    return response

def getRegisterMonthPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-comments'
    statistics_active = 'active'
    register_active = 'active'
    dict = getRegisterMonthDictFromDataBase()
    response =  render(request, "Display/subview/register_month.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'register_active':register_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'registermonth_dict':dict} )
    return response

def getRegisterYearPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-comments'
    statistics_active = 'active'
    register_active = 'active'
    dict = getRegisterYearDictFromDataBase()
    response =  render(request, "Display/subview/register_year.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'register_active':register_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'registeryear_dict':dict} )
    return response

def getZombieReturnDayPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-pencil'
    statistics_active = 'active'
    zombiereturn_active = 'active'
    dict = getZombieReturnDayDictFromDataBase()
    response =  render(request, "Display/subview/zombie_return_day.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'zombiereturn_active':zombiereturn_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'zombiereturn_day_dict':dict} )
    return response

def getZombieReturnMonthPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-pencil'
    statistics_active = 'active'
    zombiereturn_active = 'active'
    dict = getZombieReturnMonthDictFromDataBase()
    response =  render(request, "Display/subview/zombie_return_month.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'zombiereturn_active':zombiereturn_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'zombiereturn_month_dict':dict} )
    return response

def getZombieReturnYearPage(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'in nav nav-stacked'

    SecTitleIcon = 'icon-comments'
    statistics_active = 'active'
    register_active = 'active'
    dict = getZombieReturnYearDictFromDataBase()
    response =  render(request, "Display/subview/zombie_return_year.html", {'statistics_active' : statistics_active, 'sec_title_icon' :SecTitleIcon
                                                                        ,'register_active':register_active,
                                                                       'online_stack':online_stack, 'statistics_stack':statistics_stack
                                                                       ,'zombiereturn_year_dict':dict} )
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
