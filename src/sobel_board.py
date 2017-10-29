import socket
import struct
import numpy as np
import sys
import argparse
sys.path.append("/home/root/opencv_build/lib")
import cv2 as cv

parser = argparse.ArgumentParser()
parser.add_argument('-p', dest='port', default=64265, help='Intel Galileo port', type=int)
args = parser.parse_args()

s = socket.socket()
s.bind(('', args.port))
s.listen(1)

conn, addr = s.accept()

def sendImg(img):
    rows = img.shape[0]
    cols = img.shape[1]
    channels = img.shape[2] if len(img.shape) > 2 else 1

    conn.send(struct.pack('iii', rows, cols, channels) + img.tostring())

def read(size):
    batchSize = 4096
    data = ''
    while len(data) < size:
        data += conn.recv(min(size - len(data), batchSize))
    return data

def receiveImg():
    rows, cols, channels = struct.unpack('iii', read(4*3))
    data = read(rows * cols * channels)
    return np.fromstring(data, dtype=np.uint8).reshape([rows, cols, channels])


while True:
    try:
        img = receiveImg()
        img = cv.Sobel(img, ddepth=-1, dx=2, dy=2, ksize=5)
        sendImg(img)
    except Exception as e:
        print e
        conn.close()
        conn, addr = s.accept()
