---
layout: default
title: Changelog
---

# Changelog

{% for release in site.github.releases %}

## [{{ release.tag_name }}]({{ release.html_url }}) - {{ release.published_at | date: "%B %d, %Y" }}

{{ release.body | markdownify }}

---
{% endfor %}