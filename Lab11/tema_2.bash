#!/bin/bash

python3 tema_2.py << EOF
ip a | grep inet | wc -l
EOF
