FROM ubuntu:18.04

RUN apt update && DEBIAN_FRONTEND=noninteractive apt install -y --force-yes apache2 php libapache2-mod-php php-xml mc less

RUN a2enmod rewrite

RUN echo '<Directory "/var/www/html">' >> /etc/apache2/conf-enabled/99local.conf
RUN echo   'AllowOverride All' >> /etc/apache2/conf-enabled/99local.conf
RUN echo '</Directory>' >> /etc/apache2/conf-enabled/99local.conf

ENTRYPOINT service apache2 start && tail -f /var/log/apache2/*.log