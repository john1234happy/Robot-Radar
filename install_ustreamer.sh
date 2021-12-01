#!/bin/bash

sudo apt-get update

sudo apt-get install libevent-dev -y
sudo apt-get install libjpeg8-dev -y
sudo apt-get install libbsd-dev -y

sudo git clone --depth=1 https://github.com/pikvm/ustreamer
cd ustreamer
sudo make 

sudo make install

#bash write-ustream-conf.sh
#cp ./config.txt /usr/lib/systemd/system/ustreamer.service
#systemctl daemon-reload
#service ustreamer restart
