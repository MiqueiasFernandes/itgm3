import './vendor.ts';

import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { Ng2Webstorage } from 'ng2-webstorage';

import { ItgmSharedModule, UserRouteAccessService } from './shared';
import { ItgmHomeModule } from './home/home.module';
import { ItgmAdminModule } from './admin/admin.module';
import { ItgmAccountModule } from './account/account.module';
import { ItgmEntityModule } from './entities/entity.module';

import { LayoutRoutingModule } from './layouts';
import { customHttpProvider } from './blocks/interceptor/http.provider';
import { PaginationConfig } from './blocks/config/uib-pagination.config';

import {
    JhiMainComponent,
    NavbarComponent,
    FooterComponent,
    ProfileService,
    PageRibbonComponent,
    ActiveMenuDirective,
    ErrorComponent
} from './layouts';
import { FabAddComponent } from './layouts/fab-add/fab-add.component';
import { ShareComponent } from './layouts/share/share.component';
import { SidebarComponent } from './layouts/sidebar/sidebar.component';
import { FolderComponent } from './layouts/sidebar/folder/folder.component';

@NgModule({
    imports: [
        BrowserModule,
        LayoutRoutingModule,
        Ng2Webstorage.forRoot({ prefix: 'jhi', separator: '-'}),
        ItgmSharedModule,
        ItgmHomeModule,
        ItgmAdminModule,
        ItgmAccountModule,
        ItgmEntityModule
    ],
    declarations: [
        JhiMainComponent,
        NavbarComponent,
        ErrorComponent,
        PageRibbonComponent,
        ActiveMenuDirective,
        FooterComponent,
        FabAddComponent,
        ShareComponent,
        SidebarComponent,
        FolderComponent
    ],
    providers: [
        ProfileService,
        customHttpProvider(),
        PaginationConfig,
        UserRouteAccessService
    ],
    bootstrap: [ JhiMainComponent ]
})
export class ItgmAppModule {}
