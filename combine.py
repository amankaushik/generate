import subprocess
import os
import webbrowser
import sys

f = open('nodes.txt', 'w')
f.close()
f1 = open('relations.txt', 'w')
f1.close()
sections = []
vol = ['python', 'add_to_graph_combine.py', '', '', '3', '3']
directory = "/home/chanakya/NetBeansProjects/Concepto/UploadedFiles/"
with open(sys.argv[1]) as fp:
    for line in fp:
        sections.append(line)

vol[2] = directory + sections[0].strip('\n') + '_OllieOutput.txt'
#print 'VOL 2:', vol[2], ''
vol[3] = '2'
#print 'VOL: ', vol, ''
p = subprocess.Popen(vol, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
out, err = p.communicate()
#print 'OUT: ', out
sections = sections[1:]

for section in sections:
  vol[2] = directory + section.strip('\n') + '_OllieOutput.txt'
  #print 'VOL 2:', vol[2], ''
  vol[3] = str(int(out.strip()))
  #print 'VOL 3: ', vol, ''
  p = subprocess.Popen(vol, stdout=subprocess.PIPE, stderr=subprocess.PIPE)
  out, err = p.communicate()

with open('nodes.txt', 'rb+') as filehandle:
    filehandle.seek(-2, os.SEEK_END)
    filehandle.truncate()

with open('relations.txt', 'rb+') as filehandle:
    filehandle.seek(-2, os.SEEK_END)
    filehandle.truncate()

orig_stdout = sys.stdout
f2 = open("/home/chanakya/NetBeansProjects/Concepto/web/new_chap_graph.json", "w")
sys.stdout = f2

print """{
  "graph": [],
  "links": [ """

f4 = open('relations.txt', 'r')
print f4.read()

print "],"
print ''' "nodes": [ 
  {"id": "Book", "type": "circle"},'''

f5 = open('nodes.txt', 'r')
print f5.read()

print '''],
  "directed": false,
  "multigraph": false
}'''
f2.close()