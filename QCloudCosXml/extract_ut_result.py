# -*- coding:utf-8 -*-
# <div class="counter">.*</div>
import re


def extract(str):
    return str.replace('<div class="counter">', '')\
            .replace('</div>', '')


with open('./cosxml/build/reports/androidTests/connected/flavors/NORMAL/index.html', 'r') as file:
    ut_report = file.read()
    ut_result = re.findall('<div class="counter">.*</div>', ut_report)
    if len(ut_result) == 3:
        test_count = extract(ut_result[0])
        failed_count = extract(ut_result[1])
        duration = extract(ut_result[2])
    print("测试用例：" + test_count + "，失败：" + failed_count + "，耗时：" + duration)