#!/bin/bash
ip4="$(/sbin/ip -o -4 addr list wlan0 | awk '{print $4}' | cut -d/ -f1)"
echo $ip4
/home/pi/test/Robot-Radar/ustreamer/ustreamer --device=/dev/video0 -r 1080x720 --flip-vertical=1 --host=$ip4 --port=8080
