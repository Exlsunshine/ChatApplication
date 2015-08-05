
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


def home(request):
    home_active = 'active'
    SecTitleIcon = 'icon-dashboard'
    response =  render(request, "home.html", {'home_active' : home_active, 'sec_title_icon' :SecTitleIcon})
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
    return response

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

def getRealTimeOnlinePage(request):
    realtimeonline_active = 'active'
    SecTitleIcon = 'icon-edit'
    response =  render(request, "Display/subview/realtime_online.html", {'realtimeonline_active' : realtimeonline_active, 'sec_title_icon' :SecTitleIcon} )
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
    return response

def getPerHourOnlinePage(request):
    list = getPerHourOnLineNumFromDataBase()
    perhouronline_active = 'active'
    SecTitleIcon = 'icon-edit'
    response =  render(request, "Display/subview/perhour_online.html", {'perhouronline_active' : perhouronline_active, 'sec_title_icon' :SecTitleIcon, 'perhouronline_list' : list} )
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
    else:
        return home(request)
