#
# Makefile:
#	libpi4j - Java (JNI) library wrapper for WiringPi for the Raspberry Pi
#

TARGET=libpi4j.so

#DEBUG	= -g -O0
DEBUG	= -O3
CC	= gcc
INCLUDE	= -I. -I/$(JAVA_HOME)/include -I/usr/local/include -I. -I/$(JAVA_HOME)/include/linux -I/usr/local/include/linux
CFLAGS	= $(DEBUG) -Wall $(INCLUDE) -Winline -pipe $(LIBS) 

LIBS    = -L/usr/local/lib /usr/local/lib/libwiringPiDev.so /usr/local/lib/libbcm2835.a

# Should not alter anything below this line
###############################################################################

SRC	=   com_pi4j_wiringpi_DHTSensor.c

OBJ	=   com_pi4j_wiringpi_DHTSensor.o

all:		$(TARGET)

$(TARGET):	$(OBJ)
	@echo [LINK] $(OBJ)
	@$(CC) $(OBJ) -shared -o $(TARGET) $(INCLUDE) $(LIBS)  
	@size   $(TARGET)

.c.o:
	@echo [COMPILE] "$(CC) -c $(CFLAGS)" $<
	@$(CC) -c $(CFLAGS) $< -o $@

clean:
	rm -f $(OBJ) $(TARGET) *~ core tags Makefile.bak

tags:	$(SRC)
	@echo [ctags]
	@ctags $(SRC)

depend:
	makedepend -Y $(SRC)

install:	$(TARGET)
	@echo [install]
	install -m 0755 -d /usr/local/lib
	install -m 0755 -d /usr/local/include
	install -m 0644 $(TARGET)	/usr/local/lib
	# install -m 0644 pi4j.jar	/usr/share/java

uninstall:
	@echo [uninstall]
	rm -f /usr/local/lib/$(TARGET)
	# rm -f /usr/share/java/pi4j.jar


# DO NOT DELETE
com_pi4j_wiringpi_DHTSensor.o: com_pi4j_wiringpi_DHTSensor.h
