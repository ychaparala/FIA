import multiprocessing
import logging
import sys
import subprocess

def calpi():
    name = multiprocessing.current_process().name
    p = multiprocessing.current_process()
    print 'Starting:', p.name, p.pid
    process = subprocess.Popen(["/Users/yuga/Downloads/spark-2.3.1-bin-hadoop2.7/bin/spark-submit", "--master", "local[*]", "--class", "com.discover.sparkapp.SparkApp", "/Users/yuga/Downloads/pi.jar"], stdout=subprocess.PIPE)
    output, error = process.communicate()
    print output
    print 'Exiting :', p.name, p.pid
    sys.stdout.flush()

if __name__ == '__main__':
    multiprocessing.log_to_stderr(logging.INFO)
    service = multiprocessing.Process(name='calculate_PI', target=calpi)
    service2 = multiprocessing.Process(name='calculate_PI2', target=calpi)
    service.start()
    service2.start()
