
from django.shortcuts import render
from django.http import HttpResponse



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

def dynamic(request):
    response =  render(request, "Display/charts/realtime_online.html")
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
    return response

def getNum(request):
    import random
    random = random.randint(10,20)
    return HttpResponse(random)

def getRealTimeOnlinePage(request):
    realtimeonline_active = 'active'
    SecTitleIcon = 'icon-edit'
    response =  render(request, "Display/subview/realtime_online.html", {'realtimeonline_active' : realtimeonline_active, 'sec_title_icon' :SecTitleIcon} )
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"
    return response

def subview(request):
    pageName = request.GET['pageName']
    if (pageName == 'RealTimeOnline'):
        return getRealTimeOnlinePage(request)
    else:
        return home(request)
