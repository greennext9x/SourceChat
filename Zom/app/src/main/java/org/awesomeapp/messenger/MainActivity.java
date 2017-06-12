/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.awesomeapp.messenger;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.RemoteException;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;

import org.awesomeapp.messenger.model.ImConnection;
import org.awesomeapp.messenger.provider.Imps;
import org.awesomeapp.messenger.service.IChatSession;
import org.awesomeapp.messenger.service.IChatSessionManager;
import org.awesomeapp.messenger.service.IImConnection;
import org.awesomeapp.messenger.service.ImServiceConstants;
import org.awesomeapp.messenger.tasks.AddContactAsyncTask;
import org.awesomeapp.messenger.tasks.ChatSessionInitTask;
import org.awesomeapp.messenger.ui.AccountFragment;
import org.awesomeapp.messenger.ui.AccountsActivity;
import org.awesomeapp.messenger.ui.AddContactActivity;
import org.awesomeapp.messenger.ui.BaseActivity;
import org.awesomeapp.messenger.ui.ContactsListFragment;
import org.awesomeapp.messenger.ui.ContactsPickerActivity;
import org.awesomeapp.messenger.ui.ConversationDetailActivity;
import org.awesomeapp.messenger.ui.ConversationListFragment;
import org.awesomeapp.messenger.ui.legacy.SettingActivity;
import org.awesomeapp.messenger.ui.onboarding.OnboardingActivity;
import org.awesomeapp.messenger.ui.onboarding.OnboardingManager;
import org.awesomeapp.messenger.util.AssetUtil;
import org.awesomeapp.messenger.util.SecureMediaStore;
import org.awesomeapp.messenger.util.SystemServices;
import org.awesomeapp.messenger.util.XmppUriHelper;
import org.ironrabbit.type.CustomTypefaceManager;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import im.zom.messenger.R;
import info.guardianproject.iocipher.VirtualFileSystem;

/**
 * TODO
 */
public class MainActivity extends BaseActivity {

    private ViewPager mViewPager;
    private TabLayout mTabLayout;
    private FloatingActionButton mFab;
    private Toolbar mToolbar;

    private ImApp mApp;

    public final static int REQUEST_ADD_CONTACT = 9999;
    public final static int REQUEST_CHOOSE_CONTACT = REQUEST_ADD_CONTACT + 1;
    public final static int REQUEST_CHANGE_SETTINGS = REQUEST_CHOOSE_CONTACT + 1;

    private ConversationListFragment mConversationList;
    private ContactsListFragment mContactList;
    private AccountFragment mAccountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        final SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);

        if (settings.getBoolean("prefBlockScreenshots", true))
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE,
                    WindowManager.LayoutParams.FLAG_SECURE);


        setContentView(R.layout.awesome_activity_main);

        mApp = (ImApp) getApplication();

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mViewPager = (ViewPager) findViewById(R.id.viewpager);
        mTabLayout = (TabLayout) findViewById(R.id.tabs);

        setSupportActionBar(mToolbar);

        setTitle(getString(R.string.app_name_zom));

        final ActionBar ab = getSupportActionBar();

        mConversationList = new ConversationListFragment();
        mContactList = new ContactsListFragment();
        mAccountFragment = new AccountFragment();

        Adapter adapter = new Adapter(getSupportFragmentManager());
        adapter.addFragment(mConversationList, getString(R.string.title_chats), R.drawable.ic_message_white_36dp);
        adapter.addFragment(mContactList, getString(R.string.contacts), R.drawable.ic_people_white_36dp);

        mAccountFragment = new AccountFragment();

        adapter.addFragment(mAccountFragment, getString(R.string.title_me), R.drawable.ic_face_white_24dp);

        mViewPager.setAdapter(adapter);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));

        TabLayout.Tab tab = mTabLayout.newTab();
        tab.setIcon(R.drawable.ic_discuss);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setIcon(R.drawable.ic_people_white_36dp);
        mTabLayout.addTab(tab);

        tab = mTabLayout.newTab();
        tab.setIcon(R.drawable.ic_face_white_24dp);
        mTabLayout.addTab(tab);

        mTabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                mViewPager.setCurrentItem(tab.getPosition());

                setToolbarTitle(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                setToolbarTitle(tab.getPosition());
            }
        });

        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int tabIdx = mViewPager.getCurrentItem();

                if (tabIdx == 0) {

                    if (mContactList.getContactCount() > 0) {
                        Intent intent = new Intent(MainActivity.this, ContactsPickerActivity.class);
                        startActivityForResult(intent, REQUEST_CHOOSE_CONTACT);
                    } else {
                        inviteContact();
                    }

                } else if (tabIdx == 1) {
                    inviteContact();
                } else if (tabIdx == 2) {
                    startPhotoTaker();
                }


            }
        });

        setToolbarTitle(1);

        //don't wnat this to happen to often


        installRingtones();

        applyStyle();
    }

    private void installRingtones() {
        AssetUtil.installRingtone(getApplicationContext(), R.raw.bell, "Zom Bell");
        AssetUtil.installRingtone(getApplicationContext(), R.raw.chant, "Zom Chant");
        AssetUtil.installRingtone(getApplicationContext(), R.raw.yak, "Zom Yak");
        AssetUtil.installRingtone(getApplicationContext(), R.raw.dranyen, "Zom Dranyen");

    }

    private void setToolbarTitle(int tabPosition) {
        StringBuffer sb = new StringBuffer();
        sb.append(getString(R.string.app_name_zom));
        sb.append(" | ");

        switch (tabPosition) {
            case 0:
                sb.append(getString(R.string.chats));
                break;
            case 1:
                sb.append(getString(R.string.friends));
                break;
            case 2:
                sb.append(getString(R.string.me_title));
                break;
        }

        mToolbar.setTitle(sb.toString());

        if (mFab != null) {
            mFab.setVisibility(View.VISIBLE);

            if (tabPosition == 1) {
                mFab.setImageResource(R.drawable.ic_person_add_white_36dp);
            } else if (tabPosition == 2) {
                mFab.setVisibility(View.GONE);
            } else {
                mFab.setImageResource(R.drawable.ic_add_white_24dp);
            }
        }

    }

    public void inviteContact() {
        Intent i = new Intent(MainActivity.this, AddContactActivity.class);
        startActivityForResult(i, MainActivity.REQUEST_ADD_CONTACT);
    }


    @Override
    protected void onResume() {
        super.onResume();

        //if VFS is not mounted, then send to WelcomeActivity
        if (!VirtualFileSystem.get().isMounted()) {
            finish();
            startActivity(new Intent(this, RouterActivity.class));

        } else {
            ImApp app = (ImApp) getApplication();

            mApp.maybeInit(this);
            mApp.initAccountInfo();

        }

        handleIntent();

        if (!checkConnection()) {
            Snackbar sb = Snackbar.make(mViewPager, R.string.error_suspended_connection, Snackbar.LENGTH_LONG);
            sb.setAction(getString(R.string.connect), new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, AccountsActivity.class);
                    startActivity(i);
                }
            });
            sb.show();

        }

    }

    private boolean checkConnection() {
        try {
            if (mApp.getDefaultProviderId() != -1) {
                IImConnection conn = mApp.getConnection(mApp.getDefaultProviderId(), mApp.getDefaultAccountId());

                if (conn.getState() == ImConnection.DISCONNECTED
                        || conn.getState() == ImConnection.SUSPENDED
                        || conn.getState() == ImConnection.SUSPENDING)
                    return false;
            }

            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        setIntent(intent);

        handleIntent();
    }

    private void handleIntent() {

        Intent intent = getIntent();

        if (intent != null) {
            Uri data = intent.getData();
            String type = intent.getType();
            if (data != null && Imps.Chats.CONTENT_ITEM_TYPE.equals(type)) {

                long chatId = ContentUris.parseId(data);

                Intent intentChat = new Intent(this, ConversationDetailActivity.class);
                intentChat.putExtra("id", chatId);
                startActivity(intentChat);
            } else if (Imps.Contacts.CONTENT_ITEM_TYPE.equals(type)) {
                long providerId = intent.getLongExtra(ImServiceConstants.EXTRA_INTENT_PROVIDER_ID, mApp.getDefaultProviderId());
                long accountId = intent.getLongExtra(ImServiceConstants.EXTRA_INTENT_ACCOUNT_ID, mApp.getDefaultAccountId());
                String username = intent.getStringExtra(ImServiceConstants.EXTRA_INTENT_FROM_ADDRESS);
                startChat(providerId, accountId, username, true, true);
            } else if (intent.hasExtra("username")) {
                //launch a new chat based on the intent value
                startChat(mApp.getDefaultProviderId(), mApp.getDefaultAccountId(), intent.getStringExtra("username"), true, true);
            }

            setIntent(null);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == REQUEST_CHANGE_SETTINGS) {
                finish();
                startActivity(new Intent(this, MainActivity.class));
            } else if (requestCode == REQUEST_ADD_CONTACT) {

                String username = data.getStringExtra(ContactsPickerActivity.EXTRA_RESULT_USERNAME);
                long providerId = data.getLongExtra(ContactsPickerActivity.EXTRA_RESULT_PROVIDER, -1);
                long accountId = data.getLongExtra(ContactsPickerActivity.EXTRA_RESULT_ACCOUNT, -1);

                startChat(providerId, accountId, username, false, false);

            } else if (requestCode == REQUEST_CHOOSE_CONTACT) {
                String username = data.getStringExtra(ContactsPickerActivity.EXTRA_RESULT_USERNAME);

                if (username != null) {
                    long providerId = data.getLongExtra(ContactsPickerActivity.EXTRA_RESULT_PROVIDER, -1);
                    long accountId = data.getLongExtra(ContactsPickerActivity.EXTRA_RESULT_ACCOUNT, -1);

                    startChat(providerId, accountId, username, true, true);
//                } else {
//
//                    ArrayList<String> users = data.getStringArrayListExtra(ContactsPickerActivity.EXTRA_RESULT_USERNAMES);
//                    if (users != null) {
//                        //start group and do invite here
////                        startGroupChat(users);
//                    }

                }
            } else if (requestCode == ConversationDetailActivity.REQUEST_TAKE_PICTURE) {
                if (mLastPhoto != null)
                    importPhoto();

            } else if (requestCode == OnboardingManager.REQUEST_SCAN) {

                ArrayList<String> resultScans = data.getStringArrayListExtra("result");
                for (String resultScan : resultScans) {

                    try {

                        String address = null;

                        if (resultScan.startsWith("xmpp:")) {
                            address = XmppUriHelper.parse(Uri.parse(resultScan)).get(XmppUriHelper.KEY_ADDRESS);
                            String fingerprint = XmppUriHelper.getOtrFingerprint(resultScan);
                            new AddContactAsyncTask(mApp.getDefaultProviderId(), mApp.getDefaultAccountId(), mApp).execute(address, fingerprint);

                        }

                        if (address != null)
                            startChat(mApp.getDefaultProviderId(), mApp.getDefaultAccountId(), address, false, false);

                        //if they are for a group chat, then add the group
                    } catch (Exception e) {
                        Log.w(ImApp.LOG_TAG, "error parsing QR invite link", e);
                    }
                }
            }
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);

        if (mLastPhoto != null)
            savedInstanceState.putString("lastphoto", mLastPhoto.toString());

    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        // Restore UI state from the savedInstanceState.
        // This bundle has also been passed to onCreate.

        String lastPhotoPath = savedInstanceState.getString("lastphoto");
        if (lastPhotoPath != null)
            mLastPhoto = Uri.parse(lastPhotoPath);
    }

    private void importPhoto() {

        // import
        SystemServices.FileInfo info = SystemServices.getFileInfoFromURI(this, mLastPhoto);
        String sessionId = "self";
        String offerId = UUID.randomUUID().toString();

        try {
            Uri vfsUri = SecureMediaStore.resizeAndImportImage(this, sessionId, mLastPhoto, info.type);

            delete(mLastPhoto);

            //adds in an empty message, so it can exist in the gallery and be forwarded
            Imps.insertMessageInDb(
                    getContentResolver(), false, new Date().getTime(), true, null, vfsUri.toString(),
                    System.currentTimeMillis(), Imps.MessageType.OUTGOING_ENCRYPTED_VERIFIED,
                    0, offerId, info.type);

            mLastPhoto = null;
        } catch (IOException ioe) {
            Log.e(ImApp.LOG_TAG, "error importing photo", ioe);
        }

    }

    private boolean delete(Uri uri) {
        if (uri.getScheme().equals("content")) {
            int deleted = getContentResolver().delete(uri, null, null);
            return deleted == 1;
        }
        if (uri.getScheme().equals("file")) {
            java.io.File file = new java.io.File(uri.toString().substring(5));
            return file.delete();
        }
        return false;
    }


    private SearchView mSearchView;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        mSearchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.menu_search));

        if (mSearchView != null) {
            mSearchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
            mSearchView.setIconifiedByDefault(false);

            SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
                public boolean onQueryTextChange(String query) {
                    mConversationList.doSearch(query);
                    return true;
                }

                public boolean onQueryTextSubmit(String query) {
                    mConversationList.doSearch(query);

                    return true;
                }
            };

            mSearchView.setOnQueryTextListener(queryTextListener);

            mSearchView.setOnCloseListener(new SearchView.OnCloseListener() {
                @Override
                public boolean onClose() {
                    mConversationList.doSearch(null);
                    return false;
                }
            });
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;

            case R.id.menu_settings:
                Intent sintent = new Intent(this, SettingActivity.class);
                startActivityForResult(sintent, REQUEST_CHANGE_SETTINGS);
                return true;

            case R.id.menu_my_account:
                Intent i = new Intent(MainActivity.this, AccountsActivity.class);
                startActivity(i);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    static class Adapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragments = new ArrayList<>();
        private final List<String> mFragmentTitles = new ArrayList<>();
        private final List<Integer> mFragmentIcons = new ArrayList<>();

        public Adapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title, int icon) {
            mFragments.add(fragment);
            mFragmentTitles.add(title);
            mFragmentIcons.add(icon);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitles.get(position);
        }
    }

    public void startChat(long providerId, long accountId, String username, boolean startCrypto, final boolean openChat) {

        if (username != null)
            new ChatSessionInitTask(((ImApp) getApplication()), providerId, accountId, Imps.Contacts.TYPE_NORMAL, startCrypto) {
                @Override
                protected void onPostExecute(Long chatId) {

                    if (chatId != -1 && openChat) {
                        Intent intent = new Intent(MainActivity.this, ConversationDetailActivity.class);
                        intent.putExtra("id", chatId);
                        startActivity(intent);
                    }

                    super.onPostExecute(chatId);
                }

            }.executeOnExecutor(ImApp.sThreadPoolExecutor, username);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    Uri mLastPhoto = null;

    void startPhotoTaker() {

        // create Intent to take a picture and return control to the calling application
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "cs_" + new Date().getTime() + ".jpg");
        mLastPhoto = Uri.fromFile(photo);
        intent.putExtra(MediaStore.EXTRA_OUTPUT,
                mLastPhoto);

        // start the image capture Intent
        startActivityForResult(intent, ConversationDetailActivity.REQUEST_TAKE_PICTURE);
    }

    /**
     * @Override public void onConfigurationChanged(Configuration newConfig) {
     * super.onConfigurationChanged(newConfig);
     * setContentView(R.layout.awesome_activity_main);
     * <p>
     * }
     */

    public void applyStyle() {

        //first set font
        checkCustomFont();
        Typeface typeface = CustomTypefaceManager.getCurrentTypeface(this);

        if (typeface != null) {
            for (int i = 0; i < mToolbar.getChildCount(); i++) {
                View view = mToolbar.getChildAt(i);
                if (view instanceof TextView) {
                    TextView tv = (TextView) view;

                    tv.setTypeface(typeface);
                    break;
                }
            }
        }

   }

    private void checkCustomFont() {

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        List<InputMethodInfo> mInputMethodProperties = imm.getEnabledInputMethodList();

        final int N = mInputMethodProperties.size();
        boolean loadTibetan = false;
        for (int i = 0; i < N; i++) {
            InputMethodInfo imi = mInputMethodProperties.get(i);

            //imi contains the information about the keyboard you are using
            if (imi.getPackageName().equals("org.ironrabbit.bhoboard")) {
                //                    CustomTypefaceManager.loadFromKeyboard(this);
                loadTibetan = true;
                break;
            }
        }
        CustomTypefaceManager.loadFromAssets(this, loadTibetan);
    }
}
