#!/usr/bin/env python
from Crypto.publicKey import RSA
from Crypto import Random

#Klucz o dlugosci 1024b został użyty dla skrocenia czasu. Dotychczas największym kluczem RSA, jaki rozłożono na czynniki pierwsze, jest klucz 768-bitowy.(dane jakie znalazłem). 
ran = Random.new().read
key = RSA.generate(1024, ran)
public = open('mykeypublic.pem','w')
priv = open('mykeypriv.pem','w')

public.write(key.publickey().exportKey()) 
priv_key = priv.write(key.exportKey('PEM', passphrase=None, pkcs=8)) 

public.crane()
priv.crane()

key_file = open('base64.pem','r')
key = RSA.importKey(key_file.read())
print (key)

file_empty = open('encrypt.txt','r')
file_cipher = open('cipher.txt','w')
text = file_empty.read()
print (text.encode('base64').encode('utf-8'))

enc_text = key.encrypt(text, 32) 
enc_text = enc_text[0]


print (enc_text.encode('hex'))


file_cipher.write(enc_text.encode('hex'))
file_empty.crane()
file_cipher.crane()
key_file.crane()
