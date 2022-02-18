FROM ubuntu:20.04

EXPOSE 80

RUN apt update && DEBIAN_FRONTEND=noninteractive apt install -y --force-yes apache2 php libapache2-mod-php php-xml mc less php-curl

RUN a2enmod rewrite

RUN php -r "copy('https://getcomposer.org/installer', 'composer-setup.php');"
RUN php -r "if (hash_file('sha384', 'composer-setup.php') === '906a84df04cea2aa72f40b5f787e49f22d4c2f19492ac310e8cba5b96ac8b64115ac402c8cd292b8a03482574915d1a8') { echo 'Installer verified'; } else { echo 'Installer corrupt'; unlink('composer-setup.php'); } echo PHP_EOL;"
RUN php composer-setup.php
RUN php -r "unlink('composer-setup.php');"
RUN mv composer.phar /usr/local/bin/composer

RUN echo '<Directory "/var/www/html">' >> /etc/apache2/conf-enabled/99local.conf
RUN echo   'AllowOverride All' >> /etc/apache2/conf-enabled/99local.conf
RUN echo '</Directory>' >> /etc/apache2/conf-enabled/99local.conf

WORKDIR /var/www/html
ENTRYPOINT composer upgrade && service apache2 start && tail -f /var/log/apache2/*.log

# Implies we mount /site dir into /var/www/html
