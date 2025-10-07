FROM ubuntu:20.04

EXPOSE 80

RUN apt update && DEBIAN_FRONTEND=noninteractive apt install -y --force-yes apache2 php libapache2-mod-php php-xml mc less php-curl

RUN a2enmod rewrite

RUN apt install curl

RUN curl -v -sS https://getcomposer.org/installer -o composer-setup.php && php composer-setup.php && rm composer-setup.php && mv composer.phar /usr/local/bin/composer

RUN echo '<Directory "/var/www/html">' >> /etc/apache2/conf-enabled/99local.conf
RUN echo   'AllowOverride All' >> /etc/apache2/conf-enabled/99local.conf
RUN echo '</Directory>' >> /etc/apache2/conf-enabled/99local.conf

WORKDIR /var/www/html
ENTRYPOINT ln -sf vendor/undera/pwe/.htaccess && composer upgrade && service apache2 start && tail -f /var/log/apache2/*.log

# Implies we mount /site dir into /var/www/html
# docker build . -t jpgc && docker run -it -p 8080:80 -v `pwd`:/var/www/html jpgc