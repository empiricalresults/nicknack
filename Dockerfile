FROM ruby:2.1

# python is already installed (via ruby dep i think)

# next node
ENV NODE_VERSION 0.10.33
RUN curl -SLO "http://nodejs.org/dist/v$NODE_VERSION/node-v$NODE_VERSION-linux-x64.tar.gz" \
    && tar -xzf "node-v$NODE_VERSION-linux-x64.tar.gz" -C /usr/local --strip-components=1 \
    && npm install -g npm@"$NPM_VERSION" \
    && npm cache clear

# python deps
# something seems to add python above, now install pip
RUN curl -SLO "https://bootstrap.pypa.io/get-pip.py" \
    && python get-pip.py
RUN pip install Pygments

# ruby deps
RUN gem install github-pages:33

RUN mkdir /site
WORKDIR /site


