#!/usr/bin/env bash
if [ "$TRAVIS_BRANCH" = 'master' ] && [ "$TRAVIS_PULL_REQUEST" == 'false' ]; then
openssl aes-256-cbc -K $encrypted_dfff16f38589_key -iv $encrypted_dfff16f38589_iv -in cd/codesigning.asc.enc -out cd/codesigning.asc -d
fi