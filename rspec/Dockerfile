FROM ruby:3.1.0-slim-buster

RUN gem install bundler

WORKDIR /opt/jacoco-control-poc

COPY Gemfile .
COPY Gemfile.lock .
RUN bundle install

COPY . .

ENTRYPOINT ["rspec", "."]
