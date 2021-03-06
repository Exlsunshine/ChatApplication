import tornado.httpserver
import tornado.ioloop
import tornado.options
import tornado.web

from tornado.options import define, options

define("port", default = 8887, help = "run on the given port", type = int)


BASE_DIRECTOR = "C:/Users/USER007/Desktop/IM/data/"

class DownloadResponseHandler(tornado.web.RequestHandler):
    def get(self):
		print("Requesting file:\t" + self.request.uri)
		request_file = open(BASE_DIRECTOR + self.request.uri, 'rb+')
		file_data = request_file.read()
		self.write(file_data)
		self.finish()
		print("Response over.\n")

def main():
    tornado.options.parse_command_line()
    application = tornado.web.Application([ (r"/.*", DownloadResponseHandler), ])
    http_server = tornado.httpserver.HTTPServer(application)
    http_server.listen(options.port)
    tornado.ioloop.IOLoop.current().start()

if __name__ == "__main__":
    print("\t\t\tServer is now running\t\t\t")
    main()