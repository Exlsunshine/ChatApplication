from django.shortcuts import render

# Create your views here.
def line(request):
    return render(request, "line.html")

def column(request):
    sexDict = {'male' : 30.34, 'female' : 40.32}
    return render(request, "column.html", {'sexDict' : sexDict})

def skip(request):
    return render(request, "skip.html")


def home(request):
    response =  render(request, "home.html")
    response['Cache-Control'] = "no-cache"
    response['Cache-Control'] = "no-store"
    response['Pragma'] = "no-cache"

    return render(request, "home.html")