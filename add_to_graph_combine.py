# your code goes here#!/usr/bin/env python
import sys
import json
#from py2neo import neo4j

def parse_output(filename):
    #print 'FILENAME: ', filename, ''
    file = open(filename, 'r')
    extractions = file.readlines()
    pairs = []
    #print "running"
    for extraction in extractions:
        extraction.strip("\n")
        pairs.append(extraction.split("\t"))
    #print extraction.split("\t")
    add_to_database(pairs)
    #print pairs
    return pairs

def add_to_database(pairs):
    i = 0
    arr = []
    data = []
    test = []
    final_p1 = []
    final_p2 = []
    count_arr = []
    for p in pairs:
        pass
    arr.append([])
    arr[i].append(p[0])
    arr[i].append(0)
    i=i+1
    for p in pairs:
        for a in arr:
            if((p[0] in a) == True):
                a[1] = a[1] + 1
    max = 0
    count = 0
    for b in arr:
        if(b[1]>max):
            max = b[1]
    i=0
    j=0
    for c in arr:
        for b in arr:
            if(b[1] == max-i):
                if(max>int(sys.argv[3]) and i>int(sys.argv[4])):
                    break           
                test.append(pairs[count][0])
                if((pairs[count][0] in data)== False):
                    data.append(pairs[count][0])
                    j = j+1
            count = count+1
    i = i+1
    count = 0
    for t in data:
        for p in pairs:
            if(p[0] == t):
                final_p1.append(p[1])
                final_p2.append(p[2])
    for t in test:
        for a in arr:
            if(t == a[0]):
                count_arr.append(a[1])
                break
    orig_stdout = sys.stdout
    f1 = open("relations.txt", "a")
    sys.stdout = f1
    s = int(sys.argv[2])
    t = s+1
    i = 0
    j = 0
    add = 0
    pr = 0
    print "{\"source\":" + "0" + ", \"rel\": \"" + "Section" + "\", \"target\": " + str(int(sys.argv[2])-1) + "},"
    print "{\"source\":" + str(int(sys.argv[2])-1) + ", \"rel\": \"" + final_p1[add] + "\", \"target\": " + sys.argv[2] + "},"
    for d in range(len(test)):
        if(data[i]==test[j]):
            if(pr == 0):
                print "{\"source\":" +str(s) + ", \"rel\": \"" + final_p1[add] + "\", \"target\": " + str(t) + "}," 
                t = t+1 
                add = add+1
            j = j+1
            pr = 0
        else:
            print "{\"source\":" + str(int(sys.argv[2])-1) + ", \"rel\": \"" + "Topic" + "\", \"target\": " + str(t) + "},"
            s = t
            t = s+1
            i = i+1
            print "{\"source\":" +str(s) + ", \"rel\": \"" + final_p1[add] + "\", \"target\": " + str(t) + "},"
            add = add + 1
            t = t+1
            pr = 1
    f1.close()
    f2 = open("nodes.txt", "a") 
    sys.stdout = f2
    print "{\"id\": \"" + sys.argv[1][sys.argv[1].rfind('/') + 1 : sys.argv[1].rfind('_')] + "\", \"type\": \"circle\"},"
    i = 0
    j = 0
    t1 = 0
    add = 0
    for d in range(len(test)):
        if(data[i]==test[j]):
            if(t1==0):
                print "{\"id\": \"" +data[i] + "\", \"type\": \"circle\"},"
                t1=1
            print "{\"id\": \"" +final_p2[add].rstrip('\n') + "\", \"type\": \"circle\"},"      
            j = j+1
            add = add+1 
        else:
            i = i+1
            t1 = 0
    f2.close()
    sys.stdout = orig_stdout
    print t+1
if __name__ == '__main__':
    if len(sys.argv) < 2:
        print 'No file specified.'
    else:
        parse_output(sys.argv[1])
