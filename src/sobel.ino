void setup() {
    // Setup ethernet port.
    system("telnetd -l /bin/sh");
    system("ifconfig eth0 169.254.1.1 netmask 255.255.0.0 up");
    system("ifconfig -a > /dev/ttyGS0");
}

void loop() {
}
