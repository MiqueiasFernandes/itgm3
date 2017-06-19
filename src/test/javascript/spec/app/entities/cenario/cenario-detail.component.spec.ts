import { ComponentFixture, TestBed, async, inject } from '@angular/core/testing';
import { OnInit } from '@angular/core';
import { DatePipe } from '@angular/common';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs/Rx';
import { DateUtils, DataUtils, EventManager } from 'ng-jhipster';
import { ItgmTestModule } from '../../../test.module';
import { MockActivatedRoute } from '../../../helpers/mock-route.service';
import { CenarioDetailComponent } from '../../../../../../main/webapp/app/entities/cenario/cenario-detail.component';
import { CenarioService } from '../../../../../../main/webapp/app/entities/cenario/cenario.service';
import { Cenario } from '../../../../../../main/webapp/app/entities/cenario/cenario.model';

describe('Component Tests', () => {

    describe('Cenario Management Detail Component', () => {
        let comp: CenarioDetailComponent;
        let fixture: ComponentFixture<CenarioDetailComponent>;
        let service: CenarioService;

        beforeEach(async(() => {
            TestBed.configureTestingModule({
                imports: [ItgmTestModule],
                declarations: [CenarioDetailComponent],
                providers: [
                    DateUtils,
                    DataUtils,
                    DatePipe,
                    {
                        provide: ActivatedRoute,
                        useValue: new MockActivatedRoute({id: 123})
                    },
                    CenarioService,
                    EventManager
                ]
            }).overrideComponent(CenarioDetailComponent, {
                set: {
                    template: ''
                }
            }).compileComponents();
        }));

        beforeEach(() => {
            fixture = TestBed.createComponent(CenarioDetailComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(CenarioService);
        });


        describe('OnInit', () => {
            it('Should call load all on init', () => {
            // GIVEN

            spyOn(service, 'find').and.returnValue(Observable.of(new Cenario(10)));

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.find).toHaveBeenCalledWith(123);
            expect(comp.cenario).toEqual(jasmine.objectContaining({id:10}));
            });
        });
    });

});
