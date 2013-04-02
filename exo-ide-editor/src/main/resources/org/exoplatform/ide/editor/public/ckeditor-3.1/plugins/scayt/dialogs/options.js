﻿/*
 Copyright (c) 2003-2010, CKSource - Frederico Knabben. All rights reserved.
 For licensing, see LICENSE.html or http://ckeditor.com/license
 */

CKEDITOR.dialog.add('scaytcheck', function (a) {
    var b = true, c, d = CKEDITOR.document, e = [], f, g = [], h = false, i = ['dic_create,dic_restore', 'dic_rename,dic_delete'], j = ['mixedCase', 'mixedWithDigits', 'allCaps', 'ignoreDomainNames'];

    function k() {
        return document.forms.optionsbar.options;
    };
    function l() {
        return document.forms.languagesbar.scayt_lang;
    };
    function m(x, y) {
        if (!x)return;
        var z = x.length;
        if (z == undefined) {
            x.checked = x.value == y.toString();
            return;
        }
        for (var A = 0; A < z; A++) {
            x[A].checked = false;
            if (x[A].value == y.toString())x[A].checked = true;
        }
    };
    var n = [
        {id: 'options', label: a.lang.scayt.optionsTab, elements: [
            {type: 'html', id: 'options', html: '<form name="optionsbar"><div class="inner_options">\t<div class="messagebox"></div>\t<div style="display:none;">\t\t<input type="checkbox" name="options"  id="allCaps" />\t\t<label for="allCaps" id="label_allCaps"></label>\t</div>\t<div style="display:none;">\t\t<input name="options" type="checkbox"  id="ignoreDomainNames" />\t\t<label for="ignoreDomainNames" id="label_ignoreDomainNames"></label>\t</div>\t<div style="display:none;">\t<input name="options" type="checkbox"  id="mixedCase" />\t\t<label for="mixedCase" id="label_mixedCase"></label>\t</div>\t<div style="display:none;">\t\t<input name="options" type="checkbox"  id="mixedWithDigits" />\t\t<label for="mixedWithDigits" id="label_mixedWithDigits"></label>\t</div></div></form>'}
        ]},
        {id: 'langs', label: a.lang.scayt.languagesTab, elements: [
            {type: 'html', id: 'langs', html: '<form name="languagesbar"><div class="inner_langs">\t<div class="messagebox"></div>\t   <div style="float:left;width:45%;margin-left:5px;" id="scayt_lcol" ></div>   <div style="float:left;width:45%;margin-left:15px;" id="scayt_rcol"></div></div></form>'}
        ]},
        {id: 'dictionaries', label: a.lang.scayt.dictionariesTab, elements: [
            {type: 'html', style: '', id: 'dictionaries', html: '<form name="dictionarybar"><div class="inner_dictionary" style="text-align:left; white-space:normal; width:320px; overflow: hidden;">\t<div style="margin:5px auto; width:80%;white-space:normal; overflow:hidden;" id="dic_message"> </div>\t<div style="margin:5px auto; width:80%;white-space:normal;">        <span class="cke_dialog_ui_labeled_label" >Dictionary name</span><br>\t\t<span class="cke_dialog_ui_labeled_content" >\t\t\t<div class="cke_dialog_ui_input_text">\t\t\t\t<input id="dic_name" type="text" class="cke_dialog_ui_input_text"/>\t\t</div></span></div>\t\t<div style="margin:5px auto; width:80%;white-space:normal;">\t\t\t<a style="display:none;" class="cke_dialog_ui_button" href="javascript:void(0)" id="dic_create">\t\t\t\t</a>\t\t\t<a  style="display:none;" class="cke_dialog_ui_button" href="javascript:void(0)" id="dic_delete">\t\t\t\t</a>\t\t\t<a  style="display:none;" class="cke_dialog_ui_button" href="javascript:void(0)" id="dic_rename">\t\t\t\t</a>\t\t\t<a  style="display:none;" class="cke_dialog_ui_button" href="javascript:void(0)" id="dic_restore">\t\t\t\t</a>\t\t</div>\t<div style="margin:5px auto; width:95%;white-space:normal;" id="dic_info"></div></div></form>'}
        ]},
        {id: 'about', label: a.lang.scayt.aboutTab, elements: [
            {type: 'html', id: 'about', style: 'margin: 5px 5px;', html: '<div id="scayt_about"></div>'}
        ]}
    ], o = {title: a.lang.scayt.title, minWidth: 360, minHeight: 220, onShow: function () {
        var x = this;
        x.data = a.fire('scaytDialog', {});
        x.options = x.data.scayt_control.option();
        x.sLang = x.data.scayt_control.sLang;
        if (!x.data || !x.data.scayt || !x.data.scayt_control) {
            alert('Error loading application service');
            x.hide();
            return;
        }
        var y = 0;
        if (b)x.data.scayt.getCaption(a.langCode || 'en', function (z) {
            if (y++ > 0)return;
            c = z;
            q.apply(x);
            r.apply(x);
            b = false;
        }); else r.apply(x);
        x.selectPage(x.data.tab);
    }, onOk: function () {
        var x = this.data.scayt_control;
        x.option(this.options);
        var y = this.chosed_lang;
        x.setLang(y);
        x.refresh();
    }, onCancel: function () {
        var x = k();
        for (f in x)x[f].checked = false;
        m(l(), '');
    }, contents: g}, p = CKEDITOR.plugins.scayt.getScayt(a);
    e = CKEDITOR.plugins.scayt.uiTabs;
    for (f in e) {
        if (e[f] == 1)g[g.length] = n[f];
    }
    if (e[2] == 1)h = true;
    var q = function () {
        var x = this, y = x.data.scayt.getLangList(), z = ['dic_create', 'dic_delete', 'dic_rename', 'dic_restore'], A = j, B;
        if (h) {
            for (B in z) {
                var C = z[B];
                d.getById(C).setHtml('<span class="cke_dialog_ui_button">' + c['button_' + C] + '</span>');
            }
            d.getById('dic_info').setHtml(c.dic_info);
        }
        if (e[0] == 1)for (B in A) {
            var D = 'label_' + A[B], E = d.getById(D);
            if ('undefined' != typeof E && 'undefined' != typeof c[D] && 'undefined' != typeof x.options[A[B]]) {
                E.setHtml(c[D]);
                var F = E.getParent();
                F.$.style.display = 'block';
            }
        }
        var G = '<p>' + c.about_throwt_image + '</p>' + '<p>' + c.version + x.data.scayt.version.toString() + '</p>' + '<p>' + c.about_throwt_copy + '</p>';
        d.getById('scayt_about').setHtml(G);
        var H = function (R, S) {
            var T = d.createElement('label');
            T.setAttribute('for', 'cke_option' + R);
            T.setHtml(S[R]);
            if (x.sLang == R)x.chosed_lang = R;
            var U = d.createElement('div'), V = CKEDITOR.dom.element.createFromHtml('<input id="cke_option' + R + '" type="radio" ' + (x.sLang == R ? 'checked="checked"' : '') + ' value="' + R + '" name="scayt_lang" />');
            V.on('click', function () {
                this.$.checked = true;
                x.chosed_lang = R;
            });
            U.append(V);
            U.append(T);
            return{lang: S[R], code: R, radio: U};
        }, I = [];
        if (e[1] == 1) {
            for (B in y.rtl)I[I.length] = H(B, y.ltr);
            for (B in y.ltr)I[I.length] = H(B, y.ltr);
            I.sort(function (R, S) {
                return S.lang > R.lang ? -1 : 1;
            });
            var J = d.getById('scayt_lcol'), K = d.getById('scayt_rcol');
            for (B = 0; B < I.length; B++) {
                var L = B < I.length / 2 ? J : K;
                L.append(I[B].radio);
            }
        }
        var M = {};
        M.dic_create = function (R, S, T) {
            var U = T[0] + ',' + T[1], V = c.err_dic_create, W = c.succ_dic_create;
            window.scayt.createUserDictionary(S, function (X) {
                v(U);
                u(T[1]);
                W = W.replace('%s', X.dname);
                t(W);
            }, function (X) {
                V = V.replace('%s', X.dname);
                s(V + '( ' + (X.message || '') + ')');
            });
        };
        M.dic_rename = function (R, S) {
            var T = c.err_dic_rename || '', U = c.succ_dic_rename || '';
            window.scayt.renameUserDictionary(S, function (V) {
                U = U.replace('%s', V.dname);
                w(S);
                t(U);
            }, function (V) {
                T = T.replace('%s', V.dname);
                w(S);
                s(T + '( ' + (V.message || '') + ' )');
            });
        };
        M.dic_delete = function (R, S, T) {
            var U = T[0] + ',' + T[1], V = c.err_dic_delete, W = c.succ_dic_delete;
            window.scayt.deleteUserDictionary(function (X) {
                W = W.replace('%s', X.dname);
                v(U);
                u(T[0]);
                w('');
                t(W);
            }, function (X) {
                V = V.replace('%s', X.dname);
                s(V);
            });
        };
        M.dic_restore = x.dic_restore || (function (R, S, T) {
            var U = T[0] + ',' + T[1], V = c.err_dic_restore, W = c.succ_dic_restore;
            window.scayt.restoreUserDictionary(S, function (X) {
                W = W.replace('%s', X.dname);
                v(U);
                u(T[1]);
                t(W);
            }, function (X) {
                V = V.replace('%s', X.dname);
                s(V);
            });
        });
        function N(R) {
            var S = d.getById('dic_name').getValue();
            if (!S) {
                s(' Dictionary name should not be empty. ');
                return false;
            }
            try {
                var T = id = R.data.getTarget().getParent(), U = T.getId();
                M[U].apply(null, [T, S, i]);
            } catch (V) {
                s(' Dictionary error. ');
            }
            return true;
        };
        var O = (i[0] + ',' + i[1]).split(','), P;
        for (B = 0, P = O.length; B < P; B += 1) {
            var Q = d.getById(O[B]);
            if (Q)Q.on('click', N, this);
        }
    }, r = function () {
        var x = this;
        if (e[0] == 1) {
            var y = k();
            for (var z = 0, A = y.length; z < A; z++) {
                var B = y[z].id, C = d.getById(B);
                if (C) {
                    y[z].checked = false;
                    if (x.options[B] == 1)y[z].checked = true;
                    if (b)C.on('click', function () {
                        x.options[this.getId()] = this.$.checked ? 1 : 0;
                    });
                }
            }
        }
        if (e[1] == 1) {
            var D = d.getById('cke_option' + x.sLang);
            m(D.$, x.sLang);
        }
        if (h) {
            window.scayt.getNameUserDictionary(function (E) {
                var F = E.dname;
                v(i[0] + ',' + i[1]);
                if (F) {
                    d.getById('dic_name').setValue(F);
                    u(i[1]);
                } else u(i[0]);
            }, function () {
                d.getById('dic_name').setValue('');
            });
            t('');
        }
    };

    function s(x) {
        d.getById('dic_message').setHtml('<span style="color:red;">' + x + '</span>');
    };
    function t(x) {
        d.getById('dic_message').setHtml('<span style="color:blue;">' + x + '</span>');
    };
    function u(x) {
        x = String(x);
        var y = x.split(',');
        for (var z = 0, A = y.length; z < A; z += 1)d.getById(y[z]).$.style.display = 'inline';
    };
    function v(x) {
        x = String(x);
        var y = x.split(',');
        for (var z = 0, A = y.length; z < A; z += 1)d.getById(y[z]).$.style.display = 'none';
    };
    function w(x) {
        d.getById('dic_name').$.value = x;
    };
    return o;
});
