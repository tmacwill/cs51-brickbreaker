#!/bin/env/python

import urllib, urllib2, xml.dom.minidom

class BrickBreakerExample:
	results_url = 'http://localhost/brickbreaker-example/blobs/results/.xml'
	download_url = 'http://localhost/brickbreaker-example/blobs/download/'

	def getDocuments(self):
		return self.getResponseAsXml(self.results_url).getElementsByTagName("blob")

	def getDocument(self, id):
		return self.getResponse(self.download_url + id)

	def getResponse(self, url):
		req = urllib2.Request(url)
		return urllib2.urlopen(req).read()

	def getResponseAsXml(self, url):
		return xml.dom.minidom.parseString(self.getResponse(url))
    

if __name__ == "__main__":
	example = BrickBreakerExample()
    
	documents = example.getDocuments()
	for document in documents:
		print 'Title: ' + document.getAttribute('title')
		print 'Contents: ' + example.getDocument(document.getAttribute('id'))
		print ''
