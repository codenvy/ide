﻿/*
 Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
 */

(function () {
    function a(d, e, f) {
        if (!e.is || !e.getCustomData('block_processed')) {
            e.is && CKEDITOR.dom.element.setMarker(f, e, 'block_processed', true);
            d.push(e);
        }
    };
    function b(d) {
        var e = [], f = d.getChildren();
        for (var g = 0; g < f.count(); g++) {
            var h = f.getItem(g);
            if (!(h.type === CKEDITOR.NODE_TEXT && /^[ \t\n\r]+$/.test(h.getText())))e.push(h);
        }
        return e;
    };
    function c(d, e) {
        var f = (function () {
            var n = CKEDITOR.tools.extend({}, CKEDITOR.dtd.$blockLimit);
            delete n.div;
            if (d.config.div_wrapTable) {
                delete n.td;
                delete n.th;
            }
            return n;
        })(), g = CKEDITOR.dtd.div;

        function h(n) {
            var o = new CKEDITOR.dom.elementPath(n).elements, p;
            for (var q = 0; q < o.length; q++) {
                if (o[q].getName() in f) {
                    p = o[q];
                    break;
                }
            }
            return p;
        };
        function i() {
            this.foreach(function (n) {
                if (/^(?!vbox|hbox)/.test(n.type)) {
                    if (!n.setup)n.setup = function (o) {
                        n.setValue(o.getAttribute(n.id) || '');
                    };
                    if (!n.commit)n.commit = function (o) {
                        var p = this.getValue();
                        if ('dir' == n.id && o.getComputedStyle('direction') == p)return;
                        if (p)o.setAttribute(n.id, p); else o.removeAttribute(n.id);
                    };
                }
            });
        };
        function j(n) {
            var o = [], p = {}, q = [], r, s = n.document.getSelection(), t = s.getRanges(), u = s.createBookmarks(), v, w, x = n.config.enterMode == CKEDITOR.ENTER_DIV ? 'div' : 'p';
            for (v = 0; v < t.length; v++) {
                w = t[v].createIterator();
                while (r = w.getNextParagraph()) {
                    if (r.getName() in f) {
                        var y, z = r.getChildren();
                        for (y = 0; y < z.count(); y++)a(q, z.getItem(y), p);
                    } else {
                        while (!g[r.getName()] && r.getName() != 'body')r = r.getParent();
                        a(q, r, p);
                    }
                }
            }
            CKEDITOR.dom.element.clearAllMarkers(p);
            var A = l(q), B, C, D;
            for (v = 0; v < A.length; v++) {
                var E = A[v][0];
                B = E.getParent();
                for (y = 1; y < A[v].length; y++)B = B.getCommonAncestor(A[v][y]);
                D = new CKEDITOR.dom.element('div', n.document);
                for (y = 0; y < A[v].length; y++) {
                    E = A[v][y];
                    while (!E.getParent().equals(B))E = E.getParent();
                    A[v][y] = E;
                }
                var F = null;
                for (y = 0; y < A[v].length; y++) {
                    E = A[v][y];
                    if (!(E.getCustomData && E.getCustomData('block_processed'))) {
                        E.is && CKEDITOR.dom.element.setMarker(p, E, 'block_processed', true);
                        if (!y)D.insertBefore(E);
                        D.append(E);
                    }
                }
                CKEDITOR.dom.element.clearAllMarkers(p);
                o.push(D);
            }
            s.selectBookmarks(u);
            return o;
        };
        function k(n) {
            var o = new CKEDITOR.dom.elementPath(n.getSelection().getStartElement()), p = o.blockLimit, q = p && p.getAscendant('div', true);
            return q;
        };
        function l(n) {
            var o = [], p = null, q, r;
            for (var s = 0; s < n.length; s++) {
                r = n[s];
                var t = h(r);
                if (!t.equals(p)) {
                    p = t;
                    o.push([]);
                }
                o[o.length - 1].push(r);
            }
            return o;
        };
        var m = [];
        return{title: d.lang.div.title, minWidth: 400, minHeight: 165, contents: [
            {id: 'info', label: d.lang.common.generalTab, title: d.lang.common.generalTab, elements: [
                {type: 'hbox', widths: ['50%', '50%'], children: [
                    {id: 'elementStyle', type: 'select', style: 'width: 100%;', label: d.lang.div.styleSelectLabel, 'default': '', items: [], setup: function (n) {
                        this.setValue(n.$.style.cssText || '');
                    }, commit: function (n) {
                        if (this.getValue())n.$.style.cssText = this.getValue(); else n.removeAttribute('style');
                    }},
                    {id: 'class', type: 'text', label: d.lang.common.cssClass, 'default': ''}
                ]}
            ]},
            {id: 'advanced', label: d.lang.common.advancedTab, title: d.lang.common.advancedTab, elements: [
                {type: 'vbox', padding: 1, children: [
                    {type: 'hbox', widths: ['50%', '50%'], children: [
                        {type: 'text', id: 'id', label: d.lang.common.id, 'default': ''},
                        {type: 'text', id: 'lang', label: d.lang.link.langCode, 'default': ''}
                    ]},
                    {type: 'hbox', children: [
                        {type: 'text', id: 'style', style: 'width: 100%;', label: d.lang.common.cssStyle, 'default': ''}
                    ]},
                    {type: 'hbox', children: [
                        {type: 'text', id: 'title', style: 'width: 100%;', label: d.lang.common.advisoryTitle, 'default': ''}
                    ]},
                    {type: 'select', id: 'dir', style: 'width: 100%;', label: d.lang.common.langDir, 'default': '', items: [
                        [d.lang.common.langDirLtr, 'ltr'],
                        [d.lang.common.langDirRtl, 'rtl']
                    ]}
                ]}
            ]}
        ], onLoad: function () {
            i.call(this);
        }, onShow: function () {
            if (e == 'editdiv') {
                var n = k(d);
                n && this.setupContent(this._element = n);
            }
        }, onOk: function () {
            if (e == 'editdiv')m = [this._element]; else m = j(d, true);
            for (var n = 0; n < m.length; n++)this.commitContent(m[n]);
            this.hide();
        }};
    };
    CKEDITOR.dialog.add('creatediv', function (d) {
        return c(d, 'creatediv');
    });
    CKEDITOR.dialog.add('editdiv', function (d) {
        return c(d, 'editdiv');
    });
})();
