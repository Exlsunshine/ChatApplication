
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
    return HttpResponse(300)


def home(request):
    online_stack = 'nav nav-stacked'
    statistics_stack =  'nav nav-stacked'


    home_active = 'active'
    SecTitleIcon = 'icon-dashboard'

    response =  render(request, "home.html", {'home_active' : home_active, 'sec_title_icon' :SecTitleIcon, 'online_stack': online_stack, 'statistics_stack':statistics_stack})
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
    return response



def getRealTimeOnlinePage(request):
    online_stack = 'in nav nav-stacked'
    statistics_stack =  'nav nav-stacked'


    realtimeonline_active = 'active'
    SecTitleIcon = 'icon-edit'
    online_active = 'active'

    response =  render(request, "Display/subview/realtime_online.html", {'realtimeonline_active' : realtimeonline_active, 'sec_title_icon' :SecTitleIcon
                                                                          ,'online_active':online_active,'online_stack':online_stack, 'statistics_stack':statistics_stack} )
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
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
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
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
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
    return response

def subview(request):
    pageName = request.GET['pageName']
    if (pageName == 'RealTimeOnline'):
        return getRealTimeOnlinePage(request)
    elif (pageName == 'PerHourOnline'):
        return getPerHourOnlinePage(request)
    elif (pageName == 'SexStatistics'):
        return getSexStatisticsPage(request)
    else:
        return home(request)
