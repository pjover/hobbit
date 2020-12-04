{{- define "labels" }}
  labels:
    date: {{ now | htmlDate }}
    release: {{ .Release.Name }}
    revision: {{ .Release.Revision | quote }}
    chart: {{ .Chart.Name }}
    version: {{ .Chart.Version }}
{{- end }}
