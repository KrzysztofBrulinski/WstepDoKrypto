# -*- coding: utf-8 -*-
#!/usr/bin/python
list = []

def xor(bin_a, bin_b):
    return "".join(chr(ord(x) ^ ord(y)) for x, y in zip(bin_a, bin_b))

def main():
    a=[]
    crib_ciph=raw_input("W ktorym wieszu znajduje sie szukane slowo? (0-11) ")
    crib_ciph =  int(crib_ciph)
    for element in list:
        x = list[crib_ciph].decode('hex')
        b = element.decode('hex')
        a.append(xor(x,b))

    crib=raw_input("Crib: ").decode('CP852')
    crib=crib.encode('utf-8')
    i=0
    for element in a:
            s= xor(element,crib)
            print str(i) + ' ' + s.decode('utf-8', errors='ignore').encode("utf-8")
            i = i + 1

def load_from_file():
    try:
        file = open('ciagi.txt','r')
        for line in file:
            list.append(line.rstrip('\n'))
        ext=' '
        while(ext!='exit()'):
            main()
            ext = raw_input("'exit()' - to close, anything to continue: ")
    finally:
        file.close()

if __name__ == '__main__':
    load_from_file()
    pass