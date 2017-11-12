import socket
import struct
import numpy as np
import cv2 as cv
import argparse

parser = argparse.ArgumentParser()
parser.add_argument('-i', dest='ip', help='Intel Galileo IP address', type=str, required=True)
parser.add_argument('-p', dest='port', default=64265, help='Intel Galileo port', type=int)
args = parser.parse_args()

s = socket.socket()

# Send image through LAN port.
def sendImg(img):
    rows = img.shape[0]
    cols = img.shape[1]
    channels = img.shape[2] if len(img.shape) > 2 else 1
    s.send(struct.pack('iii', rows, cols, channels) + img.tobytes())

# Read certain number of bytes by patches.
def read(size):
    batchSize = 4096
    data = ''
    while len(data) < size:
        data += s.recv(min(size - len(data), batchSize))
    return data

# Receive image from LAN port.
def receiveImg():
    rows, cols, channels = struct.unpack('iii', read(4*3))
    data = read(rows * cols * channels)
    return np.fromstring(data, dtype=np.uint8).reshape([rows, cols, channels])


s.connect((args.ip, args.port))  # Connect to board.

cap = cv.VideoCapture(0)  # Open camera stream.

cv.namedWindow('Frame', cv.WINDOW_NORMAL)
cv.namedWindow('Sobel', cv.WINDOW_NORMAL)
while cv.waitKey(1) < 0:
    # Read frame from camera
    has_frame, frame = cap.read()
    if not has_frame:
        break

    # Preprocessing: convert to grayscale and resize.
    frame = cv.cvtColor(frame, cv.COLOR_BGR2GRAY)
    frame = cv.resize(frame, (0, 0), fx=0.5, fy=0.5)

    sendImg(frame)      # Send image to board.
    out = receiveImg()  # Receive output

    cv.imshow('Frame', frame)  # Display output
    cv.imshow('Sobel', out)    #
